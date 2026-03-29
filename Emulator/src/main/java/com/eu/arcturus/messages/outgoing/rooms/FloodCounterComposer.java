package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class FloodCounterComposer extends MessageComposer {
    private final int time;

    public FloodCounterComposer(int time) {
        this.time = time;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.FloodCounterComposer);
        this.response.appendInt(this.time);
        return this.response;
    }

    public int getTime() {
        return time;
    }
}
