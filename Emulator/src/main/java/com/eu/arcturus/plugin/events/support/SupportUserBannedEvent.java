package com.eu.arcturus.plugin.events.support;

import com.eu.arcturus.habbohotel.modtool.ModToolBan;
import com.eu.arcturus.habbohotel.users.Habbo;

public class SupportUserBannedEvent extends SupportEvent {

    public final Habbo target;


    public final ModToolBan ban;


    public SupportUserBannedEvent(Habbo moderator, Habbo target, ModToolBan ban) {
        super(moderator);

        this.target = target;
        this.ban = ban;
    }
}