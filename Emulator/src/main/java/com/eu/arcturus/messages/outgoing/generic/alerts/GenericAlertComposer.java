package com.eu.arcturus.messages.outgoing.generic.alerts;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GenericAlertComposer extends MessageComposer {
    private final String message;

    public GenericAlertComposer(String message) {
        this.message = message;
    }

    public GenericAlertComposer(String message, Habbo habbo) {
        this.message = message.replace("%username%", habbo.getHabboInfo().getUsername());
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GenericAlertComposer);

        this.response.appendString(this.message);

        return this.response;
    }

    public String getMessage() {
        return message;
    }
}
