package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.outgoing.rooms.RoomDataComposer;

public class StalkCommand extends Command {
    public StalkCommand() {
        super("cmd_stalk", Emulator.getTexts().getValue("commands.keys.cmd_stalk").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (gameClient.getHabbo().getHabboInfo().getCurrentRoom() == null)
            return true;

        if (params.length >= 2) {
            Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(params[1]);

            if (habbo == null) {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_stalk.not_found").replace("%user%", params[1]), RoomChatMessageBubbles.ALERT);
                return true;
            }

            if (habbo.getHabboInfo().getCurrentRoom() == null) {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_stalk.not_room").replace("%user%", params[1]), RoomChatMessageBubbles.ALERT);
                return true;
            }

            if (gameClient.getHabbo().getHabboInfo().getUsername().equals(habbo.getHabboInfo().getUsername())) {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.generic.cmd_stalk.self").replace("%user%", params[1]), RoomChatMessageBubbles.ALERT);
                return true;
            }

            if (gameClient.getHabbo().getHabboInfo().getCurrentRoom() == habbo.getHabboInfo().getCurrentRoom()) {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.generic.cmd_stalk.same_room").replace("%user%", params[1]), RoomChatMessageBubbles.ALERT);
                return true;
            }

            gameClient.sendResponse(new RoomDataComposer(habbo.getHabboInfo().getCurrentRoom(), gameClient.getHabbo(), true, false));
            //gameClient.sendResponse(new ForwardToRoomComposer(habbo.getHabboInfo().getCurrentRoom().getId()));
            return true;
        } else {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_stalk.forgot_username"), RoomChatMessageBubbles.ALERT);
            return true;
        }
    }
}
