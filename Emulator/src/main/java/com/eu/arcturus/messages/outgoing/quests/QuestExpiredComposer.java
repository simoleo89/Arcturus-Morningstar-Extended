package com.eu.arcturus.messages.outgoing.quests;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class QuestExpiredComposer extends MessageComposer {
    private final boolean expired;

    public QuestExpiredComposer(boolean expired) {
        this.expired = expired;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.QuestExpiredComposer);
        this.response.appendBoolean(this.expired);
        return this.response;
    }

    public boolean isExpired() {
        return expired;
    }
}