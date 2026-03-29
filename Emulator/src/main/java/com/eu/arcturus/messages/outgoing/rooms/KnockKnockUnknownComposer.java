package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class KnockKnockUnknownComposer extends MessageComposer {
    private final Habbo habbo;

    public KnockKnockUnknownComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(478); //TODO Hardcoded header
        this.response.appendString(this.habbo.getHabboInfo().getUsername());
        this.response.appendInt(this.habbo.getHabboInfo().getId());
        return this.response;
    }
}
