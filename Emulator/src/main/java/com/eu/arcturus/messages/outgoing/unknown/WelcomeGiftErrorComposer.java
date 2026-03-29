package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class WelcomeGiftErrorComposer extends MessageComposer {
    public final static int EMAIL_INVALID = 0;
    public final static int EMAIL_LENGTH_EXCEEDED = 1;
    public final static int EMAIL_IN_USE = 3;
    public final static int EMAIL_LIMIT_CHANGE = 4;

    private final int error;

    public WelcomeGiftErrorComposer(int error) {
        this.error = error;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.WelcomeGiftErrorComposer);
        this.response.appendInt(this.error);
        return this.response;
    }

    public int getError() {
        return error;
    }
}