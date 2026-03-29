package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserRegisteredEvent extends UserEvent {

    public UserRegisteredEvent(Habbo habbo) {
        super(habbo);
    }
}
