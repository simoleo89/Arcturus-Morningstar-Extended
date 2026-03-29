package com.eu.arcturus.messages.outgoing.handshake;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class SecureLoginOKComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.SecureLoginOKComposer);
        return this.response;
    }
}
