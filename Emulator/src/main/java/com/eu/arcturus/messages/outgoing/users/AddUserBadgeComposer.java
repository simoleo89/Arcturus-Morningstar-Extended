package com.eu.arcturus.messages.outgoing.users;

import com.eu.arcturus.habbohotel.users.HabboBadge;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class AddUserBadgeComposer extends MessageComposer {
    private final HabboBadge badge;

    public AddUserBadgeComposer(HabboBadge badge) {
        this.badge = badge;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.AddUserBadgeComposer);
        this.response.appendInt(this.badge.getId());
        this.response.appendString(this.badge.getCode());
        return this.response;
    }

    public HabboBadge getBadge() {
        return badge;
    }
}
