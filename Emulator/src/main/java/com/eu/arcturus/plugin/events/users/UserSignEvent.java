package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserSignEvent extends UserEvent {


    public int sign;

    public UserSignEvent(Habbo habbo, int sign) {
        super(habbo);
        this.sign = sign;
    }
}
