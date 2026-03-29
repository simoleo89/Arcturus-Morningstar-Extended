package com.eu.arcturus.messages.incoming.rooms.users;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserHandItemComposer;

public class RoomUserDropHandItemEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();
        this.client.getHabbo().getRoomUnit().setHandItem(0);
        if (room != null) {
            room.unIdle(this.client.getHabbo());
            room.sendComposer(new RoomUserHandItemComposer(this.client.getHabbo().getRoomUnit()).compose());
        }
    }
}
