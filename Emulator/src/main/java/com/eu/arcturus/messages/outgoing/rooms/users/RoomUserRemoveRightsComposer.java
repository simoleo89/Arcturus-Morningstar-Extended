package com.eu.arcturus.messages.outgoing.rooms.users;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomUserRemoveRightsComposer extends MessageComposer {
    private final Room room;
    private final int habboId;

    public RoomUserRemoveRightsComposer(Room room, int habboId) {
        this.room = room;
        this.habboId = habboId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomUserRemoveRightsComposer);
        this.response.appendInt(this.room.getId());
        this.response.appendInt(this.habboId);
        return this.response;
    }

    public Room getRoom() {
        return room;
    }

    public int getHabboId() {
        return habboId;
    }
}
