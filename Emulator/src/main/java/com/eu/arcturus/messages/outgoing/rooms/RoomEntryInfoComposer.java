package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomEntryInfoComposer extends MessageComposer {
    private final Room room;

    public RoomEntryInfoComposer(Room room) {
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomEntryInfoComposer);
        this.response.appendInt(this.room.getId());
        this.response.appendString(this.room.getOwnerName());
        return this.response;
    }

    public Room getRoom() {
        return room;
    }
}
