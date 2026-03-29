package com.eu.arcturus.messages.outgoing.events.resolution;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class NewYearResolutionCompletedComposer extends MessageComposer {
    public final String badge;

    public NewYearResolutionCompletedComposer(String badge) {
        this.badge = badge;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.NewYearResolutionCompletedComposer);
        this.response.appendString(this.badge);
        this.response.appendString(this.badge);
        return this.response;
    }

    public String getBadge() {
        return badge;
    }
}