package com.arcturus.plugin.furnifix;

import com.eu.habbo.core.consolecommands.ConsoleCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Console command: fixinteractions <scan|fix|fixunreg|fixall|unregistered|stats>
 */
public class FixFurniConsoleCommand extends ConsoleCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixFurniConsoleCommand.class);

    public FixFurniConsoleCommand() {
        super("fixinteractions", "Fix furniture interaction types. Usage: fixinteractions [scan|fix|fixunreg|fixall|unregistered|stats]");
    }

    @Override
    public void handle(String[] args) throws Exception {
        String action = args.length > 1 ? args[1].toLowerCase() : "scan";

        switch (action) {
            case "scan": {
                LOGGER.info("=== Scanning items_base for interaction type issues ===");
                InteractionTypeFixer.FixSummary summary = InteractionTypeFixer.scan();

                LOGGER.info("Total scanned: {}", summary.totalScanned);
                LOGGER.info("Fixes available: {}", summary.fixes.size());
                LOGGER.info("Empty with no rule: {}", summary.totalInvalid);

                if (!summary.fixes.isEmpty()) {
                    LOGGER.info("--- Proposed Fixes ---");
                    for (InteractionTypeFixer.FixResult fix : summary.fixes) {
                        LOGGER.info("  {}", fix);
                    }
                    LOGGER.info("--- By Type ---");
                    summary.fixCountByType.forEach((type, count) ->
                            LOGGER.info("  {} -> {} items", type, count));
                }

                if (!summary.warnings.isEmpty()) {
                    LOGGER.info("--- Warnings ({}) ---", summary.warnings.size());
                    int shown = 0;
                    for (String w : summary.warnings) {
                        LOGGER.warn("  {}", w);
                        if (++shown >= 50) {
                            LOGGER.warn("  ... and {} more.", summary.warnings.size() - 50);
                            break;
                        }
                    }
                }

                LOGGER.info("Run 'fixinteractions fix' to apply.");
                break;
            }

            case "fix": {
                LOGGER.info("=== Applying interaction type fixes (empty/default) ===");
                InteractionTypeFixer.FixSummary summary = InteractionTypeFixer.fix();

                LOGGER.info("Total scanned: {}", summary.totalScanned);
                LOGGER.info("Total fixed: {}", summary.totalFixed);

                for (InteractionTypeFixer.FixResult fix : summary.fixes) {
                    LOGGER.info("  FIXED: {}", fix);
                }

                if (summary.totalFixed > 0) {
                    summary.fixCountByType.forEach((type, count) ->
                            LOGGER.info("  {} -> {} items", type, count));
                    LOGGER.info("Run ':update_items' in-game or restart to reload.");
                } else {
                    LOGGER.info("No fixes needed.");
                }
                break;
            }

            case "unregistered": {
                LOGGER.info("=== Items with unregistered interaction types ===");
                List<InteractionTypeFixer.FixResult> results = InteractionTypeFixer.findUnregisteredTypes();

                if (results.isEmpty()) {
                    LOGGER.info("All items have valid registered interaction types.");
                    return;
                }

                LOGGER.info("Found {} items with unregistered types:", results.size());
                for (InteractionTypeFixer.FixResult r : results) {
                    LOGGER.info("  [{}] {}: type='{}' -> suggested='{}'", r.itemId, r.itemName, r.oldType, r.newType);
                }
                break;
            }

            case "fixunreg": {
                LOGGER.info("=== Fixing unregistered interaction types ===");
                InteractionTypeFixer.FixSummary summary = InteractionTypeFixer.fixUnregistered();

                if (summary.totalFixed > 0) {
                    for (InteractionTypeFixer.FixResult fix : summary.fixes) {
                        LOGGER.info("  FIXED: {}", fix);
                    }
                    summary.fixCountByType.forEach((type, count) ->
                            LOGGER.info("  {} -> {} items", type, count));
                    LOGGER.info("Total fixed: {}. Restart or ':update_items' to reload.", summary.totalFixed);
                } else {
                    LOGGER.info("No unregistered interaction types found.");
                }
                break;
            }

            case "fixall": {
                LOGGER.info("=== Fixing ALL interaction type issues ===");
                InteractionTypeFixer.FixSummary summary = InteractionTypeFixer.fixAll();

                if (summary.totalFixed > 0) {
                    for (InteractionTypeFixer.FixResult fix : summary.fixes) {
                        LOGGER.info("  FIXED: {}", fix);
                    }
                    LOGGER.info("--- Summary ---");
                    summary.fixCountByType.forEach((type, count) ->
                            LOGGER.info("  {} -> {} items", type, count));
                    LOGGER.info("Total fixed: {}. Restart or ':update_items' to reload.", summary.totalFixed);
                } else {
                    LOGGER.info("No fixes needed. Everything looks correct.");
                }
                break;
            }

            case "stats": {
                LOGGER.info("=== Interaction Type Statistics ===");
                Map<String, Integer> stats = InteractionTypeFixer.getTypeStats();
                int total = stats.values().stream().mapToInt(Integer::intValue).sum();

                LOGGER.info("Total items: {}", total);
                LOGGER.info("Distinct types: {}", stats.size());
                LOGGER.info("--- Breakdown ---");
                stats.forEach((type, count) -> {
                    String label = type.isEmpty() ? "(empty)" : type;
                    LOGGER.info("  {:30s} {:>6d} items", label, count);
                });
                break;
            }

            default:
                LOGGER.info("Unknown action '{}'. Available: scan, fix, fixunreg, fixall, unregistered, stats", action);
                break;
        }
    }
}
