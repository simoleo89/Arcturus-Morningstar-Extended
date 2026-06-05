package com.eu.habbo.messages.outgoing.friends;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class ConsoleReadReceiptComposer extends MessageComposer {
    private final int readerId;

    public ConsoleReadReceiptComposer(int readerId) {
        this.readerId = readerId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.ConsoleReadReceiptComposer);
        this.response.appendInt(this.readerId);
        return this.response;
    }
}
