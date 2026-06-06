package com.eu.habbo.habbohotel.items;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comment-preserving, atomic, backed-up writer for furnidata name/description, keyed by
 * classname. Supports single-file and split-tier (writes the tier that currently resolves
 * the classname). Edit-only: refuses classnames absent from the furnidata.
 */
public class FurnidataWriter {
    private final Path source;        // file (single) or base dir (split-tier)
    private final boolean directory;  // true => split-tier
    private final long maxBytes;
    private final int backupKeep;

    public FurnidataWriter(Path source, boolean directory, long maxBytes, int backupKeep) {
        this.source = source;
        this.directory = directory;
        this.maxBytes = maxBytes;
        this.backupKeep = Math.max(1, backupKeep);
    }

    /** @return true if an entry for classname was found and written. */
    public boolean write(String classname, String name, String description) throws IOException {
        String cn = classname == null ? "" : classname.trim().toLowerCase(java.util.Locale.ROOT);
        if (cn.isEmpty()) return false;
        String safeName = FurnitureTextProvider.sanitize(name);
        String safeDesc = FurnitureTextProvider.sanitize(description);

        Path target = locateFile(cn);
        if (target == null) return false;

        String raw = Files.readString(target, StandardCharsets.UTF_8);
        String edited = replaceEntryFields(raw, cn, safeName, safeDesc);
        if (edited == null || edited.equals(raw)) {
            // classname not present in this file, or no change
            return edited != null && !edited.equals(raw);
        }
        backup(target);
        atomicWrite(target, edited);
        return true;
    }

    /** For single-file just returns the file; for split-tier, the tier file that contains cn. */
    private Path locateFile(String cn) throws IOException {
        if (!directory) {
            // confirm existence via the reader (size-guarded, parses the same way)
            return containsClassname(source, cn) ? source : null;
        }
        // split-tier: iterate tiers in OVERRIDE order (later tiers win); pick the last containing cn
        Path winner = null;
        for (Path tierFile : splitTierFilesInOrder()) {
            if (containsClassname(tierFile, cn)) winner = tierFile;
        }
        return winner;
    }

    private boolean containsClassname(Path file, String cn) {
        for (FurnidataEntry e : new FurnidataReader(file, maxBytes).read()) {
            if (e.classname() != null && e.classname().trim().toLowerCase(java.util.Locale.ROOT).equals(cn)) return true;
        }
        return false;
    }

    /**
     * Replace the "name" and "description" string values inside the JSON object that holds
     * "classname": "<cn>". Preserves everything else (comments, ordering, formatting).
     * Handles double- and single-quoted JSON5 keys/values. Returns null if cn not found.
     */
    static String replaceEntryFields(String raw, String cn, String name, String description) {
        // find the classname value occurrence (case-insensitive on the value)
        Pattern classProp = Pattern.compile(
            "([\"'])classname\\1\\s*:\\s*([\"'])((?:\\\\.|(?!\\2).)*)\\2", Pattern.CASE_INSENSITIVE);
        Matcher m = classProp.matcher(raw);
        int objStart = -1, objEnd = -1;
        while (m.find()) {
            String val = m.group(3).trim().toLowerCase(java.util.Locale.ROOT);
            if (!val.equals(cn)) continue;
            // expand to the enclosing { ... }
            objStart = lastUnbalancedBrace(raw, m.start());
            objEnd = matchingClose(raw, objStart);
            break;
        }
        if (objStart < 0 || objEnd < 0) return null;
        String obj = raw.substring(objStart, objEnd + 1);
        String newObj = replaceField(obj, "name", name);
        newObj = replaceField(newObj, "description", description);
        return raw.substring(0, objStart) + newObj + raw.substring(objEnd + 1);
    }

    private static String replaceField(String obj, String field, String value) {
        Pattern p = Pattern.compile(
            "(([\"'])" + Pattern.quote(field) + "\\2\\s*:\\s*)([\"'])((?:\\\\.|(?!\\3).)*)\\3");
        Matcher m = p.matcher(obj);
        if (!m.find()) return obj; // field absent → leave object as-is
        String replacement = m.group(1) + '"' + jsonEscape(value) + '"';
        return obj.substring(0, m.start()) + replacement + obj.substring(m.end());
    }

    private static int lastUnbalancedBrace(String s, int from) {
        int depth = 0;
        for (int i = from; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == '}') depth++;
            else if (c == '{') { if (depth == 0) return i; depth--; }
        }
        return -1;
    }

    private static int matchingClose(String s, int open) {
        int depth = 0; boolean inStr = false; char q = 0;
        for (int i = open; i < s.length(); i++) {
            char c = s.charAt(i);
            if (inStr) { if (c == '\\') { i++; } else if (c == q) inStr = false; continue; }
            if (c == '"' || c == '\'') { inStr = true; q = c; }
            else if (c == '{') depth++;
            else if (c == '}') { depth--; if (depth == 0) return i; }
        }
        return -1;
    }

    private static String jsonEscape(String v) {
        StringBuilder b = new StringBuilder(v.length() + 8);
        for (int i = 0; i < v.length(); i++) {
            char c = v.charAt(i);
            if (c == '"' || c == '\\') b.append('\\').append(c);
            else b.append(c);
        }
        return b.toString();
    }

    private List<Path> splitTierFilesInOrder() throws IOException {
        // Mirrors FurnidataReader split-tier resolution at a coarse level: the manifest order.
        // For the plan we reuse the reader's defaults; the concrete enumeration is implemented
        // in Task 4 alongside the split-tier test. Single-file path does not call this.
        throw new UnsupportedOperationException("implemented in Task 4");
    }

    private void backup(Path target) throws IOException {
        Path bak = target.resolveSibling(target.getFileName() + ".bak." + System.nanoTime());
        Files.copy(target, bak, StandardCopyOption.COPY_ATTRIBUTES);
        pruneBackups(target);
    }

    private void pruneBackups(Path target) throws IOException {
        String prefix = target.getFileName() + ".bak.";
        try (var stream = Files.list(target.getParent())) {
            List<Path> baks = stream.filter(p -> p.getFileName().toString().startsWith(prefix))
                .sorted(Comparator.comparingLong(p -> backupStamp(p))).toList();
            for (int i = 0; i < baks.size() - backupKeep; i++) Files.deleteIfExists(baks.get(i));
        }
    }

    private static long backupStamp(Path p) {
        String s = p.getFileName().toString();
        try { return Long.parseLong(s.substring(s.lastIndexOf('.') + 1)); } catch (Exception e) { return 0L; }
    }

    private void atomicWrite(Path target, String content) throws IOException {
        Path tmp = target.resolveSibling(target.getFileName() + ".tmp." + System.nanoTime());
        Files.writeString(tmp, content, StandardCharsets.UTF_8);
        try {
            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /** Restore the most recent backup of the (single-file) target. @return true if restored. */
    public boolean revertLastBackup() throws IOException {
        if (directory) return revertSplitTier();
        return revertFile(source);
    }

    private boolean revertFile(Path target) throws IOException {
        String prefix = target.getFileName() + ".bak.";
        try (var stream = Files.list(target.getParent())) {
            Path latest = stream.filter(p -> p.getFileName().toString().startsWith(prefix))
                .max(Comparator.comparingLong(FurnidataWriter::backupStamp)).orElse(null);
            if (latest == null) return false;
            atomicWrite(target, Files.readString(latest, StandardCharsets.UTF_8));
            return true;
        }
    }

    private boolean revertSplitTier() throws IOException {
        boolean any = false;
        for (Path f : splitTierFilesInOrder()) any |= revertFile(f);
        return any;
    }
}
