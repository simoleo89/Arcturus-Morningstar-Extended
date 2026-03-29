package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsOnStageEnding extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(1140);
        this.response.appendInt(1); //idk
        return this.response;
    }
}
