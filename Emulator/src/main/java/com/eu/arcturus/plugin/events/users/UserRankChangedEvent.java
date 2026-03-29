package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserRankChangedEvent extends UserEvent {

    public UserRankChangedEvent(Habbo habbo) {
        super(habbo);
    }
}