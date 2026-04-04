package com.arcturus.plugin.furnifix;

import com.eu.habbo.core.consolecommands.ConsoleCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Console command: fixinteractions <scan|fix|unregistered>
 */
public class FixFurniConsoleCommand extends ConsoleCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixFurniConsoleCommand.class);

    public FixFurniConsoleCommand() {
        super("fixinteractions", "Scan/fix furniture interaction types. Usage: fixinteractions [scan|fix|unregistered]");
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

                LOGGER.info("Run 'fixinteractions fix' to apply changes.");
                break;
            }

            case "fix": {
                LOGGER.info("=== Applying interaction type fixes ===");
                InteractionTypeFixer.FixSummary summary = InteractionTypeFixer.fix();

                LOGGER.info("Total scanned: {}", summary.totalScanned);
                LOGGER.info("Total fixed: {}", summary.totalFixed);

                for (InteractionTypeFixer.FixResult fix : summary.fixes) {
                    LOGGER.info("  FIXED: {}", fix);
                }

                if (summary.totalFixed > 0) {
                    LOGGER.info("Run ':update_items' in-game or restart to reload items.");
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

            default:
                LOGGER.info("Unknown action '{}'. Use: scan, fix, unregistered", action);
                break;
        }
    }
}
