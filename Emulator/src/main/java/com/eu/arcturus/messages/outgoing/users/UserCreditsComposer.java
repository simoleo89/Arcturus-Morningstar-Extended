package com.eu.arcturus.messages.outgoing.users;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class UserCreditsComposer extends MessageComposer {
    private final Habbo habbo;

    public UserCreditsComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UserCreditsComposer);
        this.response.appendString(this.habbo.getHabboInfo().getCredits() + ".0");
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
