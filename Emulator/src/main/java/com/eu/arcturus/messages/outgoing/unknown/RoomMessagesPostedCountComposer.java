package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomMessagesPostedCountComposer extends MessageComposer {
    private final Room room;
    private final int count;

    public RoomMessagesPostedCountComposer(Room room, int count) {
        this.room = room;
        this.count = count;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomMessagesPostedCountComposer);
        this.response.appendInt(this.room.getId());
        this.response.appendString(this.room.getName());
        this.response.appendInt(this.count);
        return this.response;
    }

    public Room getRoom() {
        return room;
    }

    public int getCount() {
        return count;
    }
}