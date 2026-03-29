package com.eu.arcturus.messages.outgoing.users;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class UpdateUserLookComposer extends MessageComposer {
    private final Habbo habbo;

    public UpdateUserLookComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UpdateUserLookComposer);
        this.response.appendString(this.habbo.getHabboInfo().getLook());
        this.response.appendString(this.habbo.getHabboInfo().getGender().name());
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
