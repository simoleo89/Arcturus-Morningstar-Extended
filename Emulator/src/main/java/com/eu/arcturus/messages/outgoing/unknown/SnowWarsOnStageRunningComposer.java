package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsOnStageRunningComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(3832);
        this.response.appendInt(120);
        return this.response;
    }
}
