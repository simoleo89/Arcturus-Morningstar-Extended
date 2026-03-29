package com.eu.arcturus.plugin.events.support;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.Event;

public abstract class SupportEvent extends Event {

    public Habbo moderator;


    public SupportEvent(Habbo moderator) {
        this.moderator = moderator;
    }
}