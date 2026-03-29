package com.eu.arcturus.messages.outgoing.navigator;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class CanCreateEventComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.CanCreateEventComposer);
        this.response.appendBoolean(true);
        this.response.appendInt(0);
        return this.response;
    }
}