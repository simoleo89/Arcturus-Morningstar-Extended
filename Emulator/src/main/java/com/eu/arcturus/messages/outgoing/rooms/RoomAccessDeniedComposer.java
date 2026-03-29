package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomAccessDeniedComposer extends MessageComposer {
    private final String habbo;

    public RoomAccessDeniedComposer(String habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomAccessDeniedComposer);
        this.response.appendString(this.habbo);
        return this.response;
    }

    public String getHabbo() {
        return habbo;
    }
}
