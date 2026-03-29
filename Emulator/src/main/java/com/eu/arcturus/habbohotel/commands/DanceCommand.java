package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.habbohotel.users.DanceType;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserDanceComposer;

public class DanceCommand extends Command {
    public DanceCommand() {
        super("cmd_dance", Emulator.getTexts().getValue("commands.keys.cmd_dance").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        Habbo habbo = gameClient.getHabbo();
        if (habbo == null || habbo.getHabboInfo().getCurrentRoom() == null) {
            return true;
        }

        if (params.length == 2) {
            int danceId;

            try {
                danceId = Integer.parseInt(params[1]);
            } catch (NumberFormatException e) {
                habbo.whisper(Emulator.getTexts().getValue("commands.error.cmd_dance.invalid_dance"), RoomChatMessageBubbles.ALERT);
                return true;
            }

            if (danceId < 0 || danceId > 4) {
                habbo.whisper(Emulator.getTexts().getValue("commands.error.cmd_dance.outside_bounds"), RoomChatMessageBubbles.ALERT);
                return true;
            }

            habbo.getRoomUnit().setDanceType(DanceType.values()[danceId]);
            habbo.getHabboInfo().getCurrentRoom().sendComposer(new RoomUserDanceComposer(habbo.getRoomUnit()).compose());

            String danceName = danceId == 0 ? "stop" : "dance " + danceId;
            habbo.whisper("You are now doing " + danceName + "!", RoomChatMessageBubbles.NORMAL);
        } else {
            habbo.whisper(Emulator.getTexts().getValue("commands.error.cmd_dance.usage"), RoomChatMessageBubbles.ALERT);
        }

        return true;
    }
}