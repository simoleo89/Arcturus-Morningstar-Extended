package com.eu.arcturus.messages.outgoing.generic;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class PickMonthlyClubGiftNotificationComposer extends MessageComposer {
    private final int count;

    public PickMonthlyClubGiftNotificationComposer(int count) {
        this.count = count;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.PickMonthlyClubGiftNotificationComposer);
        this.response.appendInt(this.count);
        return this.response;
    }

    public int getCount() {
        return count;
    }
}
