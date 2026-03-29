package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsRemoveUserComposer extends MessageComposer {

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(2502);
        this.response.appendInt(3);
        return this.response;
    }
}
