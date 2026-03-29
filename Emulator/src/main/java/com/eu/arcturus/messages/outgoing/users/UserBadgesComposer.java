package com.eu.arcturus.messages.outgoing.users;

import com.eu.arcturus.habbohotel.users.HabboBadge;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.ArrayList;

public class UserBadgesComposer extends MessageComposer {
    private final ArrayList<HabboBadge> badges;
    private final int habbo;

    public UserBadgesComposer(ArrayList<HabboBadge> badges, int habbo) {
        this.badges = badges;
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UserBadgesComposer);
        this.response.appendInt(this.habbo);
        synchronized (this.badges) {
            this.response.appendInt(this.badges.size());
            for (HabboBadge badge : this.badges) {
                this.response.appendInt(badge.getSlot());
                this.response.appendString(badge.getCode());
            }
        }
        return this.response;
    }

    public ArrayList<HabboBadge> getBadges() {
        return badges;
    }

    public int getHabbo() {
        return habbo;
    }
}
