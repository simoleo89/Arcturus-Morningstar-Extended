package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.habbohotel.users.DanceType;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserDanceComposer;

public class RoomDanceCommand extends Command {
    public RoomDanceCommand() {
        super("cmd_danceall", Emulator.getTexts().getValue("commands.keys.cmd_danceall").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (params.length == 2) {
            int danceId;

            try {
                danceId = Integer.parseInt(params[1]);
            } catch (Exception e) {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_danceall.invalid_dance"), RoomChatMessageBubbles.ALERT);
                return true;
            }

            if (danceId < 0 || danceId > 4) {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_danceall.outside_bounds"), RoomChatMessageBubbles.ALERT);
                return true;
            }

            for (Habbo habbo : gameClient.getHabbo().getHabboInfo().getCurrentRoom().getHabbos()) {
                habbo.getRoomUnit().setDanceType(DanceType.values()[danceId]);
                habbo.getHabboInfo().getCurrentRoom().sendComposer(new RoomUserDanceComposer(habbo.getRoomUnit()).compose());
            }
        } else {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_danceall.no_dance"), RoomChatMessageBubbles.ALERT);
        }

        return true;
    }
}
