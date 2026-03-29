package com.eu.arcturus.messages.incoming.handshake;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class IsFirstLoginOfDayComposer extends MessageComposer {
    private final boolean isFirstLoginOfDay;

    public IsFirstLoginOfDayComposer(boolean isFirstLoginOfDay) {
        this.isFirstLoginOfDay = isFirstLoginOfDay;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.IsFirstLoginOfDayComposer);
        this.response.appendBoolean(this.isFirstLoginOfDay);
        return this.response;
    }
}