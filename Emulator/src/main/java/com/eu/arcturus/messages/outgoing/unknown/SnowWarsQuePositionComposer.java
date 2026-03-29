package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsQuePositionComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(2077);
        this.response.appendInt(1);
        return this.response;
    }
}
