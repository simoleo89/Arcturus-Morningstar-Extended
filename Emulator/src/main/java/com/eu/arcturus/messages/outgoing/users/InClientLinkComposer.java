package com.eu.arcturus.messages.outgoing.users;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class InClientLinkComposer extends MessageComposer {
    private final String link;

    public InClientLinkComposer(String link) {
        this.link = link;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.InClientLinkComposer);
        this.response.appendString(this.link);
        return this.response;
    }
}
