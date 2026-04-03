package com.eu.habbo.core.consolecommands;

import com.eu.habbo.habbohotel.items.InteractionTypeFixer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixInteractionsConsoleCommand extends ConsoleCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixInteractionsConsoleCommand.class);

    public FixInteractionsConsoleCommand() {
        super("fixinteractions", "Scan and fix furniture interaction types. Usage: fixinteractions [scan|fix|unregistered]");
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
                    for (String warning : summary.warnings) {
                        LOGGER.warn("  {}", warning);
                        shown++;
                        if (shown >= 50) {
                            LOGGER.warn("  ... and {} more warnings.", summary.warnings.size() - 50);
                            break;
                        }
                    }
                }

                LOGGER.info("Use 'fixinteractions fix' to apply the changes.");
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
                    LOGGER.info("Run ':update_items' in-game or restart to reload items into memory.");
                } else {
                    LOGGER.info("No fixes needed.");
                }
                break;
            }

            case "unregistered": {
                LOGGER.info("=== Items with unregistered interaction types ===");
                var results = InteractionTypeFixer.findUnregisteredTypes();

                if (results.isEmpty()) {
                    LOGGER.info("All items have valid registered interaction types.");
                    return;
                }

                LOGGER.info("Found {} items with unregistered types:", results.size());
                for (InteractionTypeFixer.FixResult result : results) {
                    LOGGER.info("  [{}] {}: type='{}' -> suggested='{}'",
                            result.itemId, result.itemName, result.oldInteractionType, result.newInteractionType);
                }
                break;
            }

            default:
                LOGGER.info("Unknown action '{}'. Use: scan, fix, or unregistered", action);
                break;
        }
    }
}
