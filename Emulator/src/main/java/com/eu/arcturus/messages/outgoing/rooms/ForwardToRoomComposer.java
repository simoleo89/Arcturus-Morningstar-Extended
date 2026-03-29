package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class ForwardToRoomComposer extends MessageComposer {
    private final int roomId;

    public ForwardToRoomComposer(int roomId) {
        this.roomId = roomId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.ForwardToRoomComposer);
        this.response.appendInt(this.roomId);
        return this.response;
    }

    public int getRoomId() {
        return roomId;
    }
}
