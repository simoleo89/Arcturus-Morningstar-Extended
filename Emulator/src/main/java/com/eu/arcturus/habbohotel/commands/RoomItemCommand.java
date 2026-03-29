package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserHandItemComposer;

public class RoomItemCommand extends Command {
    public RoomItemCommand() {
        super("cmd_roomitem", Emulator.getTexts().getValue("commands.keys.cmd_roomitem").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        int itemId = 0;

        if (params.length >= 2) {
            try {
                itemId = Integer.parseInt(params[1]);

                if (itemId < 0) {
                    gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_roomitem.positive"), RoomChatMessageBubbles.ALERT);
                    return true;
                }
            } catch (Exception e) {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_roomitem.no_item"), RoomChatMessageBubbles.ALERT);
                return true;
            }
        }

        for (Habbo habbo : gameClient.getHabbo().getHabboInfo().getCurrentRoom().getHabbos()) {
            habbo.getRoomUnit().setHandItem(itemId);
            habbo.getHabboInfo().getCurrentRoom().sendComposer(new RoomUserHandItemComposer(habbo.getRoomUnit()).compose());
        }

        if (itemId > 0) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_roomitem.given").replace("%item%", itemId + ""), RoomChatMessageBubbles.ALERT);
            return true;
        } else {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_roomitem.removed"), RoomChatMessageBubbles.ALERT);
            return true;
        }
    }
}
