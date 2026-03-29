package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.bots.Bot;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.outgoing.inventory.InventoryBotsComposer;
import com.eu.arcturus.messages.outgoing.inventory.InventoryRefreshComposer;
import java.util.HashMap;

public class EmptyBotsInventoryCommand extends Command {
    public EmptyBotsInventoryCommand() {
        super("cmd_empty_bots", Emulator.getTexts().getValue("commands.keys.cmd_empty_bots").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (params.length == 1 || (params.length >= 2 && !params[1].equals(Emulator.getTexts().getValue("generic.yes")))) {
            if (gameClient.getHabbo().getHabboInfo().getCurrentRoom() != null) {
                if (gameClient.getHabbo().getHabboInfo().getCurrentRoom().getUserCount() > 10) {
                    gameClient.getHabbo().alert(Emulator.getTexts().getValue("commands.succes.cmd_empty_bots.verify").replace("%generic.yes%", Emulator.getTexts().getValue("generic.yes")));
                } else {
                    gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_empty_bots.verify").replace("%generic.yes%", Emulator.getTexts().getValue("generic.yes")), RoomChatMessageBubbles.ALERT);
                }
            }

            return true;
        }

        if (params.length >= 2 && params[1].equalsIgnoreCase(Emulator.getTexts().getValue("generic.yes"))) {

            Habbo habbo = (params.length == 3 && gameClient.getHabbo().hasPermission(Permission.ACC_EMPTY_OTHERS)) ? Emulator.getGameEnvironment().getHabboManager().getHabbo(params[2]) : gameClient.getHabbo();

            if (habbo != null) {
                HashMap<Integer, Bot> bots = new HashMap<>();
                bots.putAll(habbo.getInventory().getBotsComponent().getBots());
                habbo.getInventory().getBotsComponent().getBots().clear();
                bots.values().forEach(object -> {
                    Emulator.getGameEnvironment().getBotManager().deleteBot(object);
                    return true;
                });

                habbo.getClient().sendResponse(new InventoryRefreshComposer());
                habbo.getClient().sendResponse(new InventoryBotsComposer(habbo));

                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_empty_bots.cleared").replace("%username%", habbo.getHabboInfo().getUsername()), RoomChatMessageBubbles.ALERT);
            } else {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_empty_bots"), RoomChatMessageBubbles.ALERT);
            }
        }

        return true;
    }
}