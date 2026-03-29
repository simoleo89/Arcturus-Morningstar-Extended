package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.habbohotel.rooms.RoomRightLevels;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomRightsComposer extends MessageComposer {
    private final RoomRightLevels type;

    public RoomRightsComposer(RoomRightLevels type) {
        this.type = type;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomRightsComposer);
        this.response.appendInt(this.type.level);
        return this.response;
    }

    public RoomRightLevels getType() {
        return type;
    }
}
