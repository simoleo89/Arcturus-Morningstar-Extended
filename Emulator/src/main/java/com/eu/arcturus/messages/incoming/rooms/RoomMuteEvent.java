package com.eu.arcturus.messages.incoming.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.RoomMutedComposer;

public class RoomMuteEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room != null) {
            if (room.isOwner(this.client.getHabbo())) {
                room.setMuted(!room.isMuted());
                this.client.sendResponse(new RoomMutedComposer(room));
            }
        }
    }
}
