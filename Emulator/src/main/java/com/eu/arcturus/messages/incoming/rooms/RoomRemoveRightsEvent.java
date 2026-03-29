package com.eu.arcturus.messages.incoming.rooms;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class RoomRemoveRightsEvent extends MessageHandler {

    @Override
    public int getRatelimit() {
        return 250;
    }

    @Override
    public void handle() throws Exception {
        final int roomId = this.packet.readInt();

        final Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(roomId);
        if (room == null) return;

        room.removeRights(this.client.getHabbo().getHabboInfo().getId());
    }
}