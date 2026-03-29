package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsStartLobbyCounter extends MessageComposer {

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(3757);
        this.response.appendInt(5);
        return this.response;
    }
}
