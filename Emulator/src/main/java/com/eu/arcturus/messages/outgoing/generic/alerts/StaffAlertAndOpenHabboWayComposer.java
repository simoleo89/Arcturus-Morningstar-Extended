package com.eu.arcturus.messages.outgoing.generic.alerts;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class StaffAlertAndOpenHabboWayComposer extends MessageComposer {
    private final String message;

    public StaffAlertAndOpenHabboWayComposer(String message) {
        this.message = message;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.StaffAlertAndOpenHabboWayComposer);
        this.response.appendString(this.message);
        return this.response;
    }

    public String getMessage() {
        return message;
    }
}
