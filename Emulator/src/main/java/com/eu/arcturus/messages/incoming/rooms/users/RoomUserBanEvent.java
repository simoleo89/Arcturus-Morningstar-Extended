package com.eu.arcturus.messages.incoming.rooms.users;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.RoomManager;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class RoomUserBanEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int userId = this.packet.readInt();
        int roomId = this.packet.readInt();
        String banName = this.packet.readString();

        Emulator.getGameEnvironment().getRoomManager().banUserFromRoom(this.client.getHabbo(), userId, roomId, RoomManager.RoomBanTypes.valueOf(banName));
    }
}