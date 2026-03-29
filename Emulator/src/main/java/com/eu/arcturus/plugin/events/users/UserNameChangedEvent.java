package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserNameChangedEvent extends UserEvent {
    public final String oldName;


    public UserNameChangedEvent(Habbo habbo, String oldName) {
        super(habbo);

        this.oldName = oldName;
    }
}