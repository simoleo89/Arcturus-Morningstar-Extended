package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomMutedComposer extends MessageComposer {
    private final Room room;

    public RoomMutedComposer(Room room) {
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomMutedComposer);
        this.response.appendBoolean(this.room.isMuted());
        return this.response;
    }

    public Room getRoom() {
        return room;
    }
}
