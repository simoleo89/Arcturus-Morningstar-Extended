package com.eu.arcturus.messages.incoming.rooms;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class RequestHeightmapEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (this.client.getHabbo().getHabboInfo().getLoadingRoom() > 0) {
            Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(this.client.getHabbo().getHabboInfo().getLoadingRoom());

            if (room != null) {
                // If room is loading in background, wait for it to complete
                if (room.isLoadingInProgress()) {
                    room.waitForLoad();
                }
                
                Emulator.getGameEnvironment().getRoomManager().enterRoom(this.client.getHabbo(), room);
            }
        }
    }
}
