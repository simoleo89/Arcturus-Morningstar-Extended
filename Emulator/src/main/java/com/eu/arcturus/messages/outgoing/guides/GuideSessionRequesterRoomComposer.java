package com.eu.arcturus.messages.outgoing.guides;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuideSessionRequesterRoomComposer extends MessageComposer {
    private final Room room;

    public GuideSessionRequesterRoomComposer(Room room) {
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuideSessionRequesterRoomComposer);
        this.response.appendInt(this.room != null ? this.room.getId() : 0);
        return this.response;
    }

    public Room getRoom() {
        return room;
    }
}
