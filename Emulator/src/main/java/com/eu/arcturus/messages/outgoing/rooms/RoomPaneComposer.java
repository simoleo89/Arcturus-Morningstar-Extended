package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomPaneComposer extends MessageComposer {
    private final Room room;
    private final boolean roomOwner;

    public RoomPaneComposer(Room room, boolean roomOwner) {
        this.room = room;
        this.roomOwner = roomOwner;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomPaneComposer);
        this.response.appendInt(this.room.getId());
        this.response.appendBoolean(this.roomOwner);
        return this.response;
    }

    public Room getRoom() {
        return room;
    }

    public boolean isRoomOwner() {
        return roomOwner;
    }
}
