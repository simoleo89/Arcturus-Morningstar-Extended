# Furni editor — furnidata name/description editing (Server core) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Let the furni editor write a furni's display name + description into the furnidata JSON (server-authoritative), then reindex and live-broadcast via the merged `FurnitureDataReloadComposer` (header 10047).

**Architecture:** New `FurnidataWriter` (sibling of `FurnidataReader`) does comment-preserving, atomic, backed-up writes (single-file + split-tier). Two new permission-gated handlers (`FurniEditorUpdateFurnidataEvent`, `FurniEditorRevertFurnidataEvent`) run write → `FurnitureTextProvider.reindex()` → broadcast 10047 under a shared lock also used by `FurnidataWatcher`, then audit-log. `item_name` is removed from the editor's DB update whitelist.

**Tech Stack:** Java 21, Maven, Gson, JUnit 5, Netty packets, MariaDB/JDBC. This plan is the **server** half; the Nitro-V3 client is a separate plan.

**Companion spec:** `docs/superpowers/specs/2026-06-06-furni-editor-furnidata-names-design.md`.

**Note on header 10046:** the design says "drop 10046", but it does not exist on `main` (the only broadcast is `FurnitureDataReloadComposer` 10047). No server removal needed; the client plan verifies the renderer/client side.

---

## File structure

| File | Responsibility |
|---|---|
| `Database Updates/Own_Database_RunFirst/020_furnidata_edit_log.sql` (create) | audit table + 2 config keys |
| `Emulator/.../habbohotel/items/FurnidataWriter.java` (create) | comment-preserving atomic write + backup, single-file + split-tier |
| `Emulator/.../habbohotel/items/FurnidataLock.java` (create) | one shared `ReentrantLock` for write/reindex serialization |
| `Emulator/.../habbohotel/items/FurnidataWatcher.java` (modify) | acquire the shared lock around reindex |
| `Emulator/.../habbohotel/items/FurnitureTextProvider.java` (modify) | expose `isSplitTier()` / source kind for the handler |
| `Emulator/.../messages/incoming/furnieditor/FurniEditorUpdateFurnidataEvent.java` (create) | handler: gate, rate-limit, sanitize, write, reindex, broadcast, audit |
| `Emulator/.../messages/incoming/furnieditor/FurniEditorRevertFurnidataEvent.java` (create) | handler: revert last backup, reindex, broadcast, audit |
| `Emulator/.../messages/incoming/furnieditor/FurnidataAuditLog.java` (create) | INSERT into `furnidata_edit_log` |
| `Emulator/.../messages/incoming/furnieditor/FurniEditorHelper.java` (modify) | remove `item_name` from `ALLOWED_UPDATE_FIELDS` |
| `Emulator/.../messages/incoming/Incoming.java` (modify) | header ids 10046 (update-furnidata), 10048 (revert) |
| `Emulator/.../messages/PacketManager.java` (modify) | register the 2 handlers |
| `Emulator/src/test/java/.../items/FurnidataWriterTest.java` (create) | JUnit for the writer |

Header ids: incoming **10046** = `FurniEditorUpdateFurnidataEvent` (currently free), incoming **10048** = `FurniEditorRevertFurnidataEvent`. (10047 is the outgoing reload composer.)

---

## Task 1: DB migration — audit table + config keys

**Files:**
- Create: `Database Updates/Own_Database_RunFirst/020_furnidata_edit_log.sql`

- [ ] **Step 1: Write the migration**

```sql
-- 020_furnidata_edit_log.sql
-- Audit trail for furnidata name/description edits made through the furni editor,
-- plus config keys for the editor write path. NOTE: *.enabled keys elsewhere are
-- read via Boolean.parseBoolean (true/false), but these two are numeric.
CREATE TABLE IF NOT EXISTS `furnidata_edit_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `classname` varchar(255) NOT NULL,
  `action` enum('edit','revert') NOT NULL DEFAULT 'edit',
  `old_name` varchar(256) NOT NULL DEFAULT '',
  `new_name` varchar(256) NOT NULL DEFAULT '',
  `old_description` varchar(256) NOT NULL DEFAULT '',
  `new_description` varchar(256) NOT NULL DEFAULT '',
  `timestamp` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `idx_classname` (`classname`),
  INDEX `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

INSERT IGNORE INTO `emulator_settings` (`key`,`value`) VALUES
('items.furnidata.edit.backup.keep','10'),
('items.furnidata.edit.ratelimit.ms','2000');
```

- [ ] **Step 2: Apply it to the dev DB**

Run (the auto-mode may block destructive ops; this is additive so it should pass):
```
"E:/laragon/bin/mysql/mariadb-12.2.2-winx64/bin/mysql.exe" -h 127.0.0.1 -u habbo -phabbo next < "E:/Users/simol/Desktop/DEV/Arcturus-Morningstar-Extended/Database Updates/Own_Database_RunFirst/020_furnidata_edit_log.sql"
```
Expected: no error; `SELECT COUNT(*) FROM furnidata_edit_log;` returns 0; the 2 settings exist.

- [ ] **Step 3: Commit**

```bash
git add "Database Updates/Own_Database_RunFirst/020_furnidata_edit_log.sql"
git commit -m "feat(furnidata): add furnidata_edit_log audit table + editor write config keys"
```

---

## Task 2: Shared write/reindex lock

**Files:**
- Create: `Emulator/src/main/java/com/eu/habbo/habbohotel/items/FurnidataLock.java`
- Modify: `Emulator/src/main/java/com/eu/habbo/habbohotel/items/FurnidataWatcher.java` (the `onChange` reindex)

- [ ] **Step 1: Create the lock holder**

```java
package com.eu.habbo.habbohotel.items;

import java.util.concurrent.locks.ReentrantLock;

/**
 * One process-wide lock serializing every furnidata reindex and every editor-driven
 * furnidata write, so an editor write never races the file watcher's reindex and the
 * volatile index is never observed mid-swap by two writers.
 */
public final class FurnidataLock {
    public static final ReentrantLock LOCK = new ReentrantLock();
    private FurnidataLock() {}
}
```

- [ ] **Step 2: Make the watcher acquire it around reindex**

In `FurnidataWatcher.onChange()` (around L118), wrap the reindex+broadcast body:

```java
private void onChange() {
    FurnidataLock.LOCK.lock();
    try {
        List<FurnidataEntry> delta = this.provider.reindex(new FurnidataReader(source, this.maxBytes).read());
        if (delta.isEmpty()) return;
        long now = System.currentTimeMillis();
        if (now - this.lastBroadcast < this.minIntervalMs) return;
        this.lastBroadcast = now;
        FurnitureDataReloadComposer composer = (delta.size() > this.deltaCap)
            ? new FurnitureDataReloadComposer(FurnitureDataReloadComposer.MODE_RELOAD_HINT, java.util.List.of())
            : new FurnitureDataReloadComposer(FurnitureDataReloadComposer.MODE_DELTA, delta);
        broadcast(composer);
    } finally {
        FurnidataLock.LOCK.unlock();
    }
}
```
(Keep the existing field names `lastBroadcast`, `minIntervalMs`, `deltaCap`, `broadcast`. Adapt to the real body verbatim — only the `lock()/unlock()` wrapper is new.)

- [ ] **Step 3: Compile**

Run: `cd Emulator && mvn -q -o compile`
Expected: BUILD SUCCESS.

- [ ] **Step 4: Commit**

```bash
git add Emulator/src/main/java/com/eu/habbo/habbohotel/items/FurnidataLock.java Emulator/src/main/java/com/eu/habbo/habbohotel/items/FurnidataWatcher.java
git commit -m "feat(furnidata): shared lock serializing watcher reindex and editor writes"
```

---

## Task 3: FurnidataWriter — single-file write (TDD)

The writer edits the furnidata **in place by text replacement** (preserves JSON/JSON5 comments & formatting). It locates the furni object by classname using `FurnidataReader` for existence + the raw text for the edit.

**Files:**
- Create: `Emulator/src/main/java/com/eu/habbo/habbohotel/items/FurnidataWriter.java`
- Test: `Emulator/src/test/java/com/eu/habbo/habbohotel/items/FurnidataWriterTest.java`

- [ ] **Step 1: Write the failing test**

```java
package com.eu.habbo.habbohotel.items;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class FurnidataWriterTest {

    private static final String SINGLE =
        "{ \"roomitemtypes\": { \"furnitype\": [\n" +
        "  { \"id\": 1, \"classname\": \"01_caterhead\", \"name\": \"old name\", \"description\": \"old desc\" }\n" +
        "] }, \"wallitemtypes\": { \"furnitype\": [] } }";

    @Test
    void writesNameAndDescriptionByClassnameSingleFile() throws Exception {
        @TempDir Path dir = Files.createTempDirectory("fd");
        Path file = dir.resolve("FurnitureData.json");
        Files.writeString(file, SINGLE);

        FurnidataWriter w = new FurnidataWriter(file, false, 64L * 1024 * 1024, 10);
        boolean ok = w.write("01_caterhead", "Cat Head", "A cat head");

        assertTrue(ok);
        String after = Files.readString(file);
        assertTrue(after.contains("\"Cat Head\""));
        assertTrue(after.contains("\"A cat head\""));
        assertFalse(after.contains("old name"));
        // backup created
        assertTrue(Files.list(dir).anyMatch(p -> p.getFileName().toString().startsWith("FurnitureData.json.bak")));
    }

    @Test
    void rejectsUnknownClassname() throws Exception {
        @TempDir Path dir = Files.createTempDirectory("fd");
        Path file = dir.resolve("FurnitureData.json");
        Files.writeString(file, SINGLE);
        FurnidataWriter w = new FurnidataWriter(file, false, 64L * 1024 * 1024, 10);
        assertFalse(w.write("does_not_exist", "x", "y"));
    }
}
```

- [ ] **Step 2: Run to verify it fails**

Run: `cd Emulator && mvn -q -o -Dtest=FurnidataWriterTest test`
Expected: FAIL — `FurnidataWriter` does not exist / cannot resolve symbol.

- [ ] **Step 3: Implement `FurnidataWriter` (single-file path)**

```java
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
            return !edited.equals(raw);
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
```

- [ ] **Step 4: Run the test to verify it passes**

Run: `cd Emulator && mvn -q -o -Dtest=FurnidataWriterTest test`
Expected: PASS (both single-file tests). Split-tier tests are added in Task 4.

- [ ] **Step 5: Commit**

```bash
git add Emulator/src/main/java/com/eu/habbo/habbohotel/items/FurnidataWriter.java Emulator/src/test/java/com/eu/habbo/habbohotel/items/FurnidataWriterTest.java
git commit -m "feat(furnidata): FurnidataWriter single-file comment-preserving atomic write + backup"
```

---

## Task 4: FurnidataWriter — split-tier write + path-traversal (TDD)

**Files:**
- Modify: `FurnidataWriter.java` (`splitTierFilesInOrder()`), `FurnidataWriterTest.java`

- [ ] **Step 1: Add the failing split-tier tests**

```java
    private void writeManifest(Path dir, String json) throws Exception { Files.writeString(dir.resolve("manifest.json5"), json); }

    @Test
    void writesWinningTierInSplitLayout() throws Exception {
        Path base = Files.createTempDirectory("fd");
        writeManifest(base, "{ tiers: ['core','custom'] }");
        Path core = Files.createDirectories(base.resolve("core"));
        Path custom = Files.createDirectories(base.resolve("custom"));
        writeManifest(core, "{ files: ['a.json5'] }");
        writeManifest(custom, "{ files: ['a.json5'] }");
        Files.writeString(core.resolve("a.json5"),
          "{ roomitemtypes: { furnitype: [ { id:1, classname:'chair', name:'core', description:'' } ] }, wallitemtypes:{furnitype:[]} }");
        Files.writeString(custom.resolve("a.json5"),
          "{ roomitemtypes: { furnitype: [ { id:1, classname:'chair', name:'custom', description:'' } ] }, wallitemtypes:{furnitype:[]} }");

        FurnidataWriter w = new FurnidataWriter(base, true, 64L*1024*1024, 10);
        assertTrue(w.write("chair", "EDITED", "d"));
        assertTrue(Files.readString(custom.resolve("a.json5")).contains("EDITED")); // winning tier edited
        assertTrue(Files.readString(core.resolve("a.json5")).contains("'core'") || Files.readString(core.resolve("a.json5")).contains("\"core\"")); // core untouched
    }

    @Test
    void rejectsTraversalInManifest() throws Exception {
        Path base = Files.createTempDirectory("fd");
        writeManifest(base, "{ tiers: ['../escape'] }");
        Files.createDirectories(base.getParent().resolve("escape"));
        FurnidataWriter w = new FurnidataWriter(base, true, 64L*1024*1024, 10);
        assertFalse(w.write("anything", "x", "y")); // traversal tier ignored, classname not found
    }
```

- [ ] **Step 2: Run to verify it fails**

Run: `cd Emulator && mvn -q -o -Dtest=FurnidataWriterTest test`
Expected: FAIL — `UnsupportedOperationException` from `splitTierFilesInOrder`.

- [ ] **Step 3: Implement `splitTierFilesInOrder()`**

Replace the stub body with manifest-driven enumeration mirroring `FurnidataReader`'s defaults and its `isInside` guard:

```java
    private static final List<String> DEFAULT_TIERS = List.of("core", "custom", "seasonal");
    private static final List<String> MANIFEST_NAMES = List.of("manifest.json5", "manifest.json");

    private List<Path> splitTierFilesInOrder() throws IOException {
        Path baseNorm = source.toRealPath();
        List<Path> out = new java.util.ArrayList<>();
        List<String> tiers = manifestList(source, "tiers", DEFAULT_TIERS);
        for (String tier : tiers) {
            Path tierDir = safeResolve(baseNorm, tier);
            if (tierDir == null || !Files.isDirectory(tierDir)) continue;
            List<String> files = manifestList(tierDir, "files", null);
            if (files == null) { // no manifest: take *.json5/*.json in name order
                try (var s = Files.list(tierDir)) {
                    s.filter(p -> { String n = p.getFileName().toString(); return n.endsWith(".json5") || n.endsWith(".json"); })
                     .filter(p -> !p.getFileName().toString().startsWith("manifest."))
                     .sorted().forEach(out::add);
                }
            } else {
                for (String f : files) { Path fp = safeResolve(tierDir, f); if (fp != null && Files.exists(fp)) out.add(fp); }
            }
        }
        return out;
    }

    /** Resolve child under baseNorm, rejecting anything that escapes the base dir. */
    private static Path safeResolve(Path baseNorm, String child) {
        try {
            Path p = baseNorm.resolve(child).normalize();
            return p.startsWith(baseNorm) ? p : null;
        } catch (Exception e) { return null; }
    }

    private static List<String> manifestList(Path dir, String key, List<String> def) {
        for (String name : MANIFEST_NAMES) {
            Path mf = dir.resolve(name);
            if (!Files.exists(mf)) continue;
            try {
                String json = FurnidataReader.stripJson5(Files.readString(mf, StandardCharsets.UTF_8));
                com.google.gson.JsonObject o = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
                if (!o.has(key) || !o.get(key).isJsonArray()) return def;
                List<String> r = new java.util.ArrayList<>();
                o.getAsJsonArray(key).forEach(e -> r.add(e.getAsString()));
                return r;
            } catch (Exception e) { return def; }
        }
        return def;
    }
```

If `FurnidataReader.stripJson5` is currently package-private/private, widen it to `static` package-visible (it already lives in package `items`, same as the writer — confirm access; if `private`, change to package-private `static`).

- [ ] **Step 4: Run to verify it passes**

Run: `cd Emulator && mvn -q -o -Dtest=FurnidataWriterTest test`
Expected: PASS (all 4 tests).

- [ ] **Step 5: Commit**

```bash
git add Emulator/src/main/java/com/eu/habbo/habbohotel/items/FurnidataWriter.java Emulator/src/test/java/com/eu/habbo/habbohotel/items/FurnidataWriterTest.java
git commit -m "feat(furnidata): split-tier write to winning tier with path-traversal guard"
```

---

## Task 5: Provider source accessor for the handler

The handler needs to know whether the resolved source is a file or a dir, and the configured caps, to build a `FurnidataWriter`.

**Files:**
- Modify: `Emulator/.../habbohotel/items/FurnitureTextProvider.java`

- [ ] **Step 1: Add accessors (no test — thin getters)**

After `getSource()` (L63), add:

```java
    public boolean isSourceDirectory() {
        Path s = this.source;
        return s != null && java.nio.file.Files.isDirectory(s);
    }

    public long getMaxBytes() {
        return Long.parseLong(com.eu.habbo.Emulator.getConfig().getValue("items.furnidata.max.bytes", String.valueOf(64L * 1024 * 1024)));
    }

    /** Rebuild + atomically swap the index from the current source and return the delta. */
    public java.util.List<FurnidataEntry> reindexFromSource() {
        if (this.source == null) return java.util.List.of();
        return reindex(new FurnidataReader(this.source, getMaxBytes()).read());
    }
```

- [ ] **Step 2: Compile**

Run: `cd Emulator && mvn -q -o compile`
Expected: BUILD SUCCESS.

- [ ] **Step 3: Commit**

```bash
git add Emulator/src/main/java/com/eu/habbo/habbohotel/items/FurnitureTextProvider.java
git commit -m "feat(furnidata): expose source kind, maxBytes, reindexFromSource on the provider"
```

---

## Task 6: Audit-log writer

**Files:**
- Create: `Emulator/.../messages/incoming/furnieditor/FurnidataAuditLog.java`

- [ ] **Step 1: Implement (DB insert; no unit test — integration-verified in Task 9)**

```java
package com.eu.habbo.messages.incoming.furnieditor;

import com.eu.habbo.Emulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;

public final class FurnidataAuditLog {
    private static final Logger LOGGER = LoggerFactory.getLogger(FurnidataAuditLog.class);
    private FurnidataAuditLog() {}

    public static void record(int userId, String classname, String action,
                              String oldName, String newName, String oldDesc, String newDesc) {
        try (Connection c = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement st = c.prepareStatement(
                 "INSERT INTO furnidata_edit_log (user_id, classname, action, old_name, new_name, old_description, new_description, timestamp) " +
                 "VALUES (?,?,?,?,?,?,?,?)")) {
            st.setInt(1, userId);
            st.setString(2, classname);
            st.setString(3, action);
            st.setString(4, oldName == null ? "" : oldName);
            st.setString(5, newName == null ? "" : newName);
            st.setString(6, oldDesc == null ? "" : oldDesc);
            st.setString(7, newDesc == null ? "" : newDesc);
            st.setInt(8, Emulator.getIntUnixTimestamp());
            st.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Failed to write furnidata_edit_log", e);
        }
    }
}
```

- [ ] **Step 2: Compile & commit**

```bash
cd Emulator && mvn -q -o compile
git add Emulator/src/main/java/com/eu/habbo/messages/incoming/furnieditor/FurnidataAuditLog.java
git commit -m "feat(furnidata): audit-log writer for editor furnidata edits"
```

---

## Task 7: `FurniEditorUpdateFurnidataEvent` handler

**Files:**
- Create: `Emulator/.../messages/incoming/furnieditor/FurniEditorUpdateFurnidataEvent.java`
- Modify: `Incoming.java`, `PacketManager.java`

- [ ] **Step 1: Add the header id** in `messages/incoming/Incoming.java` after `FurniEditorDeleteEvent = 10045` (L439):

```java
    public static final int FurniEditorUpdateFurnidataEvent = 10046;
    public static final int FurniEditorRevertFurnidataEvent = 10048;
```

- [ ] **Step 2: Register** in `messages/PacketManager.java` after the FurniEditorDeleteEvent registration (L287):

```java
        this.registerHandler(Incoming.FurniEditorUpdateFurnidataEvent, FurniEditorUpdateFurnidataEvent.class);
        this.registerHandler(Incoming.FurniEditorRevertFurnidataEvent, FurniEditorRevertFurnidataEvent.class);
```

- [ ] **Step 3: Implement the handler**

```java
package com.eu.habbo.messages.incoming.furnieditor;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.items.*;
import com.eu.habbo.habbohotel.permissions.Permission;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.furniture.FurnitureDataReloadComposer;
import com.eu.habbo.messages.outgoing.furnieditor.FurniEditorResultComposer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class FurniEditorUpdateFurnidataEvent extends MessageHandler {

    private static final ConcurrentHashMap<Integer, Long> LAST_EDIT = new ConcurrentHashMap<>();

    @Override
    public void handle() throws Exception {
        if (!this.client.getHabbo().hasPermission(Permission.ACC_CATALOGFURNI)) {
            this.client.sendResponse(new FurniEditorResultComposer(false, "No permission"));
            return;
        }
        int itemId = this.packet.readInt();
        JsonObject body = JsonParser.parseString(this.packet.readString()).getAsJsonObject();
        String name = body.has("name") ? body.get("name").getAsString() : "";
        String description = body.has("description") ? body.get("description").getAsString() : "";

        // rate-limit per admin
        long rl = Long.parseLong(Emulator.getConfig().getValue("items.furnidata.edit.ratelimit.ms", "2000"));
        int uid = this.client.getHabbo().getHabboInfo().getId();
        long now = System.currentTimeMillis();
        Long last = LAST_EDIT.get(uid);
        if (last != null && now - last < rl) {
            this.client.sendResponse(new FurniEditorResultComposer(false, "Too fast, slow down"));
            return;
        }

        String classname = classnameForItem(itemId);
        if (classname == null) {
            this.client.sendResponse(new FurniEditorResultComposer(false, "Item not found"));
            return;
        }

        FurnitureTextProvider provider = Emulator.getGameEnvironment().getFurnitureTextProvider();
        Path source = provider.getSource();
        if (source == null) {
            this.client.sendResponse(new FurniEditorResultComposer(false, "Furnidata source not configured"));
            return;
        }
        String oldName = provider.getName(classname);

        FurnidataLock.LOCK.lock();
        try {
            FurnidataWriter writer = new FurnidataWriter(source, provider.isSourceDirectory(),
                    provider.getMaxBytes(),
                    Integer.parseInt(Emulator.getConfig().getValue("items.furnidata.edit.backup.keep", "10")));
            boolean written = writer.write(classname, name, description);
            if (!written) {
                this.client.sendResponse(new FurniEditorResultComposer(false, "Classname not present in furnidata"));
                return;
            }
            List<FurnidataEntry> delta = provider.reindexFromSource();
            if (!delta.isEmpty()) {
                FurnitureDataReloadComposer composer = (delta.size() > 500)
                        ? new FurnitureDataReloadComposer(FurnitureDataReloadComposer.MODE_RELOAD_HINT, List.of())
                        : new FurnitureDataReloadComposer(FurnitureDataReloadComposer.MODE_DELTA, delta);
                for (com.eu.habbo.habbohotel.users.Habbo h : Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos().values()) {
                    if (h.getClient() != null) h.getClient().sendResponse(composer);
                }
            }
        } finally {
            FurnidataLock.LOCK.unlock();
        }

        LAST_EDIT.put(uid, now);
        String newName = FurnitureTextProvider.sanitize(name);
        FurnidataAuditLog.record(uid, classname, "edit", oldName == null ? "" : oldName, newName, "", FurnitureTextProvider.sanitize(description));
        this.client.sendResponse(new FurniEditorResultComposer(true, "Furnidata updated", itemId));
    }

    static String classnameForItem(int itemId) {
        try (Connection c = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement st = c.prepareStatement("SELECT item_name FROM items_base WHERE id = ? LIMIT 1")) {
            st.setInt(1, itemId);
            try (ResultSet rs = st.executeQuery()) { return rs.next() ? rs.getString("item_name") : null; }
        } catch (Exception e) { return null; }
    }
}
```

- [ ] **Step 4: Compile**

Run: `cd Emulator && mvn -q -o compile`
Expected: BUILD SUCCESS. (If `FurniEditorResultComposer(boolean,String)` 2-arg ctor is absent, use the 3-arg `(false, msg, 0)` form seen in `FurniEditorUpdateEvent`.)

- [ ] **Step 5: Commit**

```bash
git add Emulator/src/main/java/com/eu/habbo/messages/incoming/furnieditor/FurniEditorUpdateFurnidataEvent.java Emulator/src/main/java/com/eu/habbo/messages/incoming/Incoming.java Emulator/src/main/java/com/eu/habbo/messages/PacketManager.java
git commit -m "feat(furnieditor): FurniEditorUpdateFurnidataEvent — write furnidata + reindex + broadcast 10047"
```

---

## Task 8: `FurniEditorRevertFurnidataEvent` handler

**Files:**
- Create: `Emulator/.../messages/incoming/furnieditor/FurniEditorRevertFurnidataEvent.java`

- [ ] **Step 1: Implement**

```java
package com.eu.habbo.messages.incoming.furnieditor;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.items.*;
import com.eu.habbo.habbohotel.permissions.Permission;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.furniture.FurnitureDataReloadComposer;
import com.eu.habbo.messages.outgoing.furnieditor.FurniEditorResultComposer;
import java.nio.file.Path;
import java.util.List;

public class FurniEditorRevertFurnidataEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (!this.client.getHabbo().hasPermission(Permission.ACC_CATALOGFURNI)) {
            this.client.sendResponse(new FurniEditorResultComposer(false, "No permission"));
            return;
        }
        int itemId = this.packet.readInt();
        String classname = FurniEditorUpdateFurnidataEvent.classnameForItem(itemId);

        FurnitureTextProvider provider = Emulator.getGameEnvironment().getFurnitureTextProvider();
        Path source = provider.getSource();
        if (source == null) { this.client.sendResponse(new FurniEditorResultComposer(false, "Furnidata source not configured")); return; }

        FurnidataLock.LOCK.lock();
        try {
            FurnidataWriter writer = new FurnidataWriter(source, provider.isSourceDirectory(), provider.getMaxBytes(),
                    Integer.parseInt(Emulator.getConfig().getValue("items.furnidata.edit.backup.keep", "10")));
            if (!writer.revertLastBackup()) { this.client.sendResponse(new FurniEditorResultComposer(false, "No backup to restore")); return; }
            List<FurnidataEntry> delta = provider.reindexFromSource();
            if (!delta.isEmpty()) {
                FurnitureDataReloadComposer composer = (delta.size() > 500)
                        ? new FurnitureDataReloadComposer(FurnitureDataReloadComposer.MODE_RELOAD_HINT, List.of())
                        : new FurnitureDataReloadComposer(FurnitureDataReloadComposer.MODE_DELTA, delta);
                for (com.eu.habbo.habbohotel.users.Habbo h : Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos().values())
                    if (h.getClient() != null) h.getClient().sendResponse(composer);
            }
        } finally { FurnidataLock.LOCK.unlock(); }

        FurnidataAuditLog.record(this.client.getHabbo().getHabboInfo().getId(), classname == null ? "?" : classname, "revert", "", "", "", "");
        this.client.sendResponse(new FurniEditorResultComposer(true, "Furnidata reverted", itemId));
    }
}
```

- [ ] **Step 2: Compile & commit**

```bash
cd Emulator && mvn -q -o compile
git add Emulator/src/main/java/com/eu/habbo/messages/incoming/furnieditor/FurniEditorRevertFurnidataEvent.java
git commit -m "feat(furnieditor): FurniEditorRevertFurnidataEvent — restore last furnidata backup + rebroadcast"
```

---

## Task 9: Lock down `item_name` (immutable) + full build/test

**Files:**
- Modify: `messages/incoming/furnieditor/FurniEditorHelper.java`

- [ ] **Step 1: Remove `item_name` from the whitelist**

In `ALLOWED_UPDATE_FIELDS` (L84-91) delete the `"item_name",` token so the set starts `"public_name", "sprite_id", ...`. (Leave `FIELD_MAP` intact; an absent whitelist entry already blocks the column.)

- [ ] **Step 2: Full build + tests**

Run: `cd Emulator && mvn -q -o package -DskipTests=false`
Expected: BUILD SUCCESS; `FurnidataWriterTest` green.

- [ ] **Step 3: Manual acceptance (against the running hotel)**

1. Start the rebuilt jar. Confirm boot has no `items.furnidata.*` errors (config restored earlier) and `FurnitureTextProvider` resolved the source.
2. From the client furni editor (or a crafted packet 10046) edit `01_caterhead` name → expect: file changed (a `.bak.*` appears next to `FurnitureData.json`), all clients’ catalog/inventory/infostand show the new name without refresh, a row in `furnidata_edit_log`.
3. Edit with a `%user.name%` in the name → stored value shows `％user.name％` (fullwidth `%`), no injection in a wired `%furni.name%` sign.
4. Send revert (packet 10048) → name restored from backup, rebroadcast.
5. Rapid double-edit within 2s → second rejected ("Too fast").

- [ ] **Step 4: Commit**

```bash
git add Emulator/src/main/java/com/eu/habbo/messages/incoming/furnieditor/FurniEditorHelper.java
git commit -m "feat(furnieditor): make item_name immutable (remove from DB update whitelist)"
```

---

## Self-review notes (addressed)

- **Spec coverage:** writer (single+split, atomic, backup, sanitize, size guard, path-traversal) → Tasks 3–4; lock → Task 2; handlers update/revert + reindex+10047 → Tasks 7–8; audit table+log → Tasks 1,6,9; rate-limit → Task 7; item_name immutable → Task 9; reused `ACC_CATALOGFURNI` → Tasks 7–8. "Drop 10046" → already absent on main (documented in header).
- **Types:** `FurnidataWriter(Path,boolean,long,int)`, `write(String,String,String):boolean`, `revertLastBackup():boolean`, `FurnitureTextProvider.reindexFromSource():List<FurnidataEntry>`, `sanitize(String):String` (static), `getName(String):String`, `FurnidataLock.LOCK` — used consistently across Tasks 3–8.
- **Open items for the plan author:** `FurniEditorResultComposer` ctor arity (2-arg vs 3-arg) — verify and adapt (noted Task 7 Step 4); `FurnidataReader.stripJson5` visibility widen to package (Task 4 Step 3).

## Out of scope (this plan)
Client UI/hook/UX (separate Nitro-V3 plan); renderer (unchanged, 10047 already merged); create/delete furnidata entries.
