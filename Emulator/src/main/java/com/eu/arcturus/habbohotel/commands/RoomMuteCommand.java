package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;

public class RoomMuteCommand extends Command {
    public RoomMuteCommand() {
        super("cmd_roommute", Emulator.getTexts().getValue("commands.keys.cmd_roommute").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        Room room = gameClient.getHabbo().getHabboInfo().getCurrentRoom();

        if (room != null) {
            room.setMuted(!room.isMuted());
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_roommute." + (room.isMuted() ? "muted" : "unmuted")), RoomChatMessageBubbles.ALERT);
        }

        return true;
    }
}