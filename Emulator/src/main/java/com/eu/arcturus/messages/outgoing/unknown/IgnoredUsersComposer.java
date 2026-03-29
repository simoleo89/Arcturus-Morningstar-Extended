package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class IgnoredUsersComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.IgnoredUsersComposer);
        this.response.appendInt(0);
        return this.response;
    }
}
