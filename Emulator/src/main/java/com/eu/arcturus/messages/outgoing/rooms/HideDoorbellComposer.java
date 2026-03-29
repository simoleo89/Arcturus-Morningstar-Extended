package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class HideDoorbellComposer extends MessageComposer {
    private final String username;

    public HideDoorbellComposer(String username) {
        this.username = username;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.HideDoorbellComposer);
        this.response.appendString(this.username);
        return this.response;
    }

    public String getUsername() {
        return username;
    }
}