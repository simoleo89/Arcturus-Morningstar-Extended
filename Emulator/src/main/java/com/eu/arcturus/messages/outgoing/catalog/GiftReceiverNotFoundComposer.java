package com.eu.arcturus.messages.outgoing.catalog;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GiftReceiverNotFoundComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GiftReceiverNotFoundComposer);
        return this.response;
    }
}
