package com.eu.arcturus.messages.incoming.hotelview;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.hotelview.HotelViewComposer;

public class HotelViewEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.getHabbo().getHabboInfo().setLoadingRoom(0);

        if (this.client.getHabbo().getHabboInfo().getCurrentRoom() != null) {
            Emulator.getGameEnvironment().getRoomManager().leaveRoom(this.client.getHabbo(), this.client.getHabbo().getHabboInfo().getCurrentRoom());
        }

        if (this.client.getHabbo().getHabboInfo().getRoomQueueId() != 0) {
            Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(this.client.getHabbo().getHabboInfo().getRoomQueueId());

            if (room != null) {
                room.removeFromQueue(this.client.getHabbo());
            } else {
                this.client.getHabbo().getHabboInfo().setRoomQueueId(0);
            }
            this.client.sendResponse(new HotelViewComposer());
        }

        if (this.client.getHabbo().getRoomUnit() != null) {
            this.client.getHabbo().getRoomUnit().clearWalking();
            this.client.getHabbo().getRoomUnit().setInRoom(false);
        }
    }
}
