package com.eu.arcturus.messages.incoming.rooms.users;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserTagsComposer;

public class RequestRoomUserTagsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int roomUnitId = this.packet.readInt();

        if (this.client.getHabbo().getHabboInfo().getCurrentRoom() != null) {
            Habbo habbo = this.client.getHabbo().getHabboInfo().getCurrentRoom().getHabboByRoomUnitId(roomUnitId);

            if (habbo != null) {
                this.client.sendResponse(new RoomUserTagsComposer(habbo));
            }
        }
    }
}
