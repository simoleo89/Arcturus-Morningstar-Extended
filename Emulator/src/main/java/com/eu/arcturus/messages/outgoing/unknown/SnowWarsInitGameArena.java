package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsInitGameArena extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(3924);
        this.response.appendInt(0);
        return this.response;
    }
}
