package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.items.InteractionTypeFixer;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;

import java.util.List;

public class FixFurniInteractionsCommand extends Command {

    public FixFurniInteractionsCommand() {
        super("cmd_fix_furni_interactions", Emulator.getTexts().getValue("commands.keys.cmd_fix_furni_interactions").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (params.length < 2) {
            gameClient.getHabbo().whisper(
                    "Usage: :fixfurni <scan|fix|unregistered>\n" +
                    "  scan - Preview changes without applying\n" +
                    "  fix - Apply all interaction type fixes\n" +
                    "  unregistered - Show items with unregistered interaction types",
                    RoomChatMessageBubbles.ALERT
            );
            return true;
        }

        String action = params[1].toLowerCase();

        switch (action) {
            case "scan": {
                gameClient.getHabbo().whisper("Scanning items_base for interaction type issues...", RoomChatMessageBubbles.ALERT);

                InteractionTypeFixer.FixSummary summary = InteractionTypeFixer.scan();

                StringBuilder sb = new StringBuilder();
                sb.append("=== Interaction Type Scan ===\r");
                sb.append("Total scanned: ").append(summary.totalScanned).append("\r");
                sb.append("Fixes available: ").append(summary.fixes.size()).append("\r");
                sb.append("Empty with no rule: ").append(summary.totalInvalid).append("\r");

                if (!summary.fixes.isEmpty()) {
                    sb.append("\r--- Proposed Fixes ---\r");
                    int shown = 0;
                    for (InteractionTypeFixer.FixResult fix : summary.fixes) {
                        sb.append(fix.toString()).append("\r");
                        shown++;
                        if (shown >= 50) {
                            sb.append("... and ").append(summary.fixes.size() - 50).append(" more.\r");
                            break;
                        }
                    }
                }

                if (!summary.warnings.isEmpty()) {
                    sb.append("\r--- Warnings (").append(summary.warnings.size()).append(") ---\r");
                    int shown = 0;
                    for (String warning : summary.warnings) {
                        sb.append(warning).append("\r");
                        shown++;
                        if (shown >= 20) {
                            sb.append("... and ").append(summary.warnings.size() - 20).append(" more.\r");
                            break;
                        }
                    }
                }

                sb.append("\rUse ':fixfurni fix' to apply the fixes.");

                gameClient.getHabbo().alert(sb.toString());
                break;
            }

            case "fix": {
                gameClient.getHabbo().whisper("Applying interaction type fixes...", RoomChatMessageBubbles.ALERT);

                InteractionTypeFixer.FixSummary summary = InteractionTypeFixer.fix();

                if (summary.totalFixed > 0) {
                    // Reload items after fix
                    Emulator.getGameEnvironment().getItemManager().loadItems();

                    gameClient.getHabbo().alert(
                            "=== Interaction Type Fix Complete ===\r" +
                            "Total scanned: " + summary.totalScanned + "\r" +
                            "Total fixed: " + summary.totalFixed + "\r" +
                            "Items reloaded into memory.\r\r" +
                            "Use ':update_items' to refresh active rooms."
                    );
                } else {
                    gameClient.getHabbo().whisper("No interaction type fixes needed.", RoomChatMessageBubbles.ALERT);
                }
                break;
            }

            case "unregistered": {
                List<InteractionTypeFixer.FixResult> results = InteractionTypeFixer.findUnregisteredTypes();

                if (results.isEmpty()) {
                    gameClient.getHabbo().whisper("All items have valid registered interaction types.", RoomChatMessageBubbles.ALERT);
                    return true;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("=== Items with Unregistered Interaction Types ===\r");
                sb.append("Found ").append(results.size()).append(" items:\r\r");

                int shown = 0;
                for (InteractionTypeFixer.FixResult result : results) {
                    sb.append(String.format("[%d] %s: type='%s' -> suggested='%s'\r",
                            result.itemId, result.itemName, result.oldInteractionType, result.newInteractionType));
                    shown++;
                    if (shown >= 50) {
                        sb.append("... and ").append(results.size() - 50).append(" more.\r");
                        break;
                    }
                }

                gameClient.getHabbo().alert(sb.toString());
                break;
            }

            default:
                gameClient.getHabbo().whisper("Unknown action. Use: scan, fix, or unregistered", RoomChatMessageBubbles.ALERT);
                break;
        }

        return true;
    }
}
