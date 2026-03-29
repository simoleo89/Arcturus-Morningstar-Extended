package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomHeightMapComposer extends MessageComposer {
    private final Room room;

    public RoomHeightMapComposer(Room room) {
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomHeightMapComposer);
        this.response.appendBoolean(true);
        this.response.appendInt(this.room.getWallHeight()); //FixedWallsHeight
        this.response.appendString(this.room.getLayout().getRelativeMap());
        return this.response;
    }

    public Room getRoom() {
        return room;
    }
}
