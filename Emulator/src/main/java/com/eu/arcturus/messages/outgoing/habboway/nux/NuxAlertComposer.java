package com.eu.arcturus.messages.outgoing.habboway.nux;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class NuxAlertComposer extends MessageComposer {
    private final String link;

    public NuxAlertComposer(String link) {
        this.link = link;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.NuxAlertComposer);
        this.response.appendString(this.link);
        return this.response;
    }

    public String getLink() {
        return link;
    }
}