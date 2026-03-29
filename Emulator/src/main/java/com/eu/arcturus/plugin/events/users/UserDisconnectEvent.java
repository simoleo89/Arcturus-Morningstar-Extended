package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserDisconnectEvent extends UserEvent {

    public UserDisconnectEvent(Habbo habbo) {
        super(habbo);
    }
}
