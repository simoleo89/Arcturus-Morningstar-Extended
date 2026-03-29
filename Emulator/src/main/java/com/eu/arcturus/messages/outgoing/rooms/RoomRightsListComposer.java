package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;
import java.util.HashMap;

import java.util.Map;

public class RoomRightsListComposer extends MessageComposer {
    private final Room room;

    public RoomRightsListComposer(Room room) {
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomRightsListComposer);
        this.response.appendInt(this.room.getId());

        HashMap<Integer, String> rightsMap = this.room.getUsersWithRights();

        this.response.appendInt(rightsMap.size());

        for (Map.Entry<Integer, String> set : rightsMap.entrySet()) {
            this.response.appendInt(set.getKey());
            this.response.appendString(set.getValue());
        }

        return this.response;
    }

    public Room getRoom() {
        return room;
    }
}
