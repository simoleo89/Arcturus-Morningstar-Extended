package com.eu.arcturus.messages.outgoing.rooms.users;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class ChangeNameUpdatedComposer extends MessageComposer {
    private final Habbo habbo;

    public ChangeNameUpdatedComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.ChangeNameUpdateComposer);
        this.response.appendInt(0);
        this.response.appendString(this.habbo.getHabboInfo().getUsername());
        this.response.appendInt(0);
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
