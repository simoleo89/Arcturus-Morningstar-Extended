package com.eu.arcturus.messages.incoming.rooms;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.FavoriteRoomChangedComposer;

public class RoomFavoriteEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int roomId = this.packet.readInt();

        Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(roomId);

        boolean added = true;
        if (room != null) {
            if (this.client.getHabbo().getHabboStats().hasFavoriteRoom(roomId)) {
                this.client.getHabbo().getHabboStats().removeFavoriteRoom(roomId);
                added = false;
            } else {
                if (!this.client.getHabbo().getHabboStats().addFavoriteRoom(roomId)) {
                    return;
                }
            }

            this.client.sendResponse(new FavoriteRoomChangedComposer(roomId, added));
        }
    }
}