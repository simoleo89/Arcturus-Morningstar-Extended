package com.eu.arcturus.messages.outgoing.habboway.nux;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class NewUserIdentityComposer extends MessageComposer {
    private final Habbo habbo;

    public NewUserIdentityComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.NewUserIdentityComposer);
        this.response.appendInt(this.habbo.noobStatus());
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
