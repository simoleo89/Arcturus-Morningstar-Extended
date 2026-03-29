package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserRightsGivenEvent extends UserEvent {
    public final Habbo receiver;

    public UserRightsGivenEvent(Habbo habbo, Habbo receiver) {
        super(habbo);

        this.receiver = receiver;
    }
}
