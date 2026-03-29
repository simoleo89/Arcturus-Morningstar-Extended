package com.eu.arcturus.messages.outgoing.rooms.users;

import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomUserRemoveComposer extends MessageComposer {
    private final RoomUnit roomUnit;

    public RoomUserRemoveComposer(RoomUnit roomUnit) {
        this.roomUnit = roomUnit;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomUserRemoveComposer);
        this.response.appendString(this.roomUnit.getId() + "");
        return this.response;
    }

    public RoomUnit getRoomUnit() {
        return roomUnit;
    }
}
