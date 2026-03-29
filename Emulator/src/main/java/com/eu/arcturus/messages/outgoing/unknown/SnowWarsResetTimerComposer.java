package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsResetTimerComposer extends MessageComposer {
    //SnowStageRunning?
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(294);
        this.response.appendInt(100);
        return this.response;
    }
}
