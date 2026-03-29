package com.eu.arcturus.messages.outgoing.handshake;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class EnableNotificationsComposer extends MessageComposer {
    private final boolean enabled;

    public EnableNotificationsComposer(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.EnableNotificationsComposer);
        this.response.appendBoolean(this.enabled);
        return this.response;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
