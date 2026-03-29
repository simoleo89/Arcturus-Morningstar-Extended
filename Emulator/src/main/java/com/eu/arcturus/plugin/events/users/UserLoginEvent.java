package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserLoginEvent extends UserEvent {

    public final String ip;

    public UserLoginEvent(Habbo habbo, String ip) {
        super(habbo);

        this.ip = ip;
    }
}
