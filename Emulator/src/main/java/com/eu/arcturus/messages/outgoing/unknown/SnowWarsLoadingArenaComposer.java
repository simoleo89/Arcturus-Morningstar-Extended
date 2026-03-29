package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsLoadingArenaComposer extends MessageComposer {
    private final int count;

    public SnowWarsLoadingArenaComposer(int count) {
        this.count = count;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(3850);
        this.response.appendInt(this.count); //GameID?
        this.response.appendInt(0); //Count
        //this.response.appendInt(1); //ItemID to dispose?
        return this.response;
    }

    public int getCount() {
        return count;
    }
}
