package com.eu.arcturus.messages.outgoing.users;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class UserAchievementScoreComposer extends MessageComposer {
    private final Habbo habbo;

    public UserAchievementScoreComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UserAchievementScoreComposer);
        this.response.appendInt(this.habbo.getHabboStats().getAchievementScore());
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
