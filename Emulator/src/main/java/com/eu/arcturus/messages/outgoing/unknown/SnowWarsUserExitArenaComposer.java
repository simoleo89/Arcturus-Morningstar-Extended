package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsUserExitArenaComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(3811);
        this.response.appendInt(1); //userId
        this.response.appendInt(1); //IDK ? TEAM?
        return this.response;
    }
}
