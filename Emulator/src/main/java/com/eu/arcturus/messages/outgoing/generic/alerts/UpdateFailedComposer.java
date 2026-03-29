package com.eu.arcturus.messages.outgoing.generic.alerts;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class UpdateFailedComposer extends MessageComposer {
    private final String message;

    public UpdateFailedComposer(String message) {
        this.message = message;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UpdateFailedComposer);
        this.response.appendString(this.message);
        return this.response;
    }

    public String getMessage() {
        return message;
    }
}
