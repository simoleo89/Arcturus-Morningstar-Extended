package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.Event;

public abstract class UserEvent extends Event {

    public final Habbo habbo;


    public UserEvent(Habbo habbo) {
        this.habbo = habbo;
    }
}
