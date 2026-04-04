package com.arcturus.plugin.furnifix;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.Command;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;

import java.util.List;

/**
 * In-game command: :fixfurni <scan|fix|unregistered|fixid>
 *
 * Requires permission "cmd_fix_furni_interactions" or falls back to rank >= 7.
 */
public class FixFurniCommand extends Command {

    private static final String PERMISSION = "cmd_fix_furni_interactions";

    public FixFurniCommand() {
        super(PERMISSION, getKeys());
    }

    private static String[] getKeys() {
        try {
            String keys = Emulator.getTexts().getValue("commands.keys.cmd_fix_furni_interactions");
            if (keys != null && !keys.isEmpty()) {
                return keys.split(";");
            }
        } catch (Exception ignored) {}
        return new String[]{"fixfurni"};
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (params.length < 2) {
            gameClient.getHabbo().whisper(
                    "Usage: :fixfurni <scan|fix|unregistered|fixid>\r" +
                    "  scan        - Preview changes without applying\r" +
                    "  fix         - Apply all interaction type fixes to DB\r" +
                    "  unregistered - Show items with unregistered types\r" +
                    "  fixid <id> <type> - Fix a single item by ID",
                    RoomChatMessageBubbles.ALERT
            );
            return true;
        }

        String action = params[1].toLowerCase();

        switch (action) {
            case "scan":
                handleScan(gameClient);
                break;

            case "fix":
                handleFix(gameClient);
                break;

            case "unregistered":
                handleUnregistered(gameClient);
                break;

            case "fixid":
                handleFixId(gameClient, params);
                break;

            default:
                gameClient.getHabbo().whisper("Unknown action '" + action + "'. Use: scan, fix, unregistered, fixid", RoomChatMessageBubbles.ALERT);
                break;
        }

        return true;
    }

    private void handleScan(GameClient gameClient) {
        gameClient.getHabbo().whisper("Scanning items_base...", RoomChatMessageBubbles.ALERT);

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
                sb.append(fix).append("\r");
                if (++shown >= 50) {
                    sb.append("... and ").append(summary.fixes.size() - 50).append(" more.\r");
                    break;
                }
            }
        }

        if (!summary.warnings.isEmpty()) {
            sb.append("\r--- Warnings (").append(summary.warnings.size()).append(") ---\r");
            int shown = 0;
            for (String w : summary.warnings) {
                sb.append(w).append("\r");
                if (++shown >= 20) {
                    sb.append("... and ").append(summary.warnings.size() - 20).append(" more.\r");
                    break;
                }
            }
        }

        sb.append("\rUse ':fixfurni fix' to apply.");
        gameClient.getHabbo().alert(sb.toString());
    }

    private void handleFix(GameClient gameClient) {
        gameClient.getHabbo().whisper("Applying fixes...", RoomChatMessageBubbles.ALERT);

        InteractionTypeFixer.FixSummary summary = InteractionTypeFixer.fix();

        if (summary.totalFixed > 0) {
            // Reload items in memory
            Emulator.getGameEnvironment().getItemManager().loadItems();

            gameClient.getHabbo().alert(
                    "=== Interaction Type Fix Complete ===\r" +
                    "Total scanned: " + summary.totalScanned + "\r" +
                    "Total fixed: " + summary.totalFixed + "\r" +
                    "Items reloaded into memory.\r\r" +
                    "Use ':update_items' to refresh active rooms."
            );
        } else {
            gameClient.getHabbo().whisper("No fixes needed. All items look correct!", RoomChatMessageBubbles.ALERT);
        }
    }

    private void handleUnregistered(GameClient gameClient) {
        List<InteractionTypeFixer.FixResult> results = InteractionTypeFixer.findUnregisteredTypes();

        if (results.isEmpty()) {
            gameClient.getHabbo().whisper("All items have valid registered interaction types.", RoomChatMessageBubbles.ALERT);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Items with Unregistered Interaction Types ===\r");
        sb.append("Found ").append(results.size()).append(" items:\r\r");

        int shown = 0;
        for (InteractionTypeFixer.FixResult r : results) {
            sb.append(String.format("[%d] %s: type='%s' -> suggested='%s'\r", r.itemId, r.itemName, r.oldType, r.newType));
            if (++shown >= 50) {
                sb.append("... and ").append(results.size() - 50).append(" more.\r");
                break;
            }
        }

        gameClient.getHabbo().alert(sb.toString());
    }

    private void handleFixId(GameClient gameClient, String[] params) {
        if (params.length < 4) {
            gameClient.getHabbo().whisper("Usage: :fixfurni fixid <item_base_id> <interaction_type>", RoomChatMessageBubbles.ALERT);
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(params[2]);
        } catch (NumberFormatException e) {
            gameClient.getHabbo().whisper("Invalid item ID: " + params[2], RoomChatMessageBubbles.ALERT);
            return;
        }

        String newType = params[3].toLowerCase();

        if (InteractionTypeFixer.fixSingle(itemId, newType)) {
            Emulator.getGameEnvironment().getItemManager().loadItems();
            gameClient.getHabbo().whisper("Item " + itemId + " fixed to '" + newType + "' and reloaded.", RoomChatMessageBubbles.ALERT);
        } else {
            gameClient.getHabbo().whisper("Failed to fix item " + itemId + ". Check that the type '" + newType + "' is valid.", RoomChatMessageBubbles.ALERT);
        }
    }
}
