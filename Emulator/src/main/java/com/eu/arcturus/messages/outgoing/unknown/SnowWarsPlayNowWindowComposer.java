package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsPlayNowWindowComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(2276);
        this.response.appendInt(0); //status
        this.response.appendInt(100);
        this.response.appendInt(0);
        this.response.appendInt(-1);
        return this.response;
    }
}
