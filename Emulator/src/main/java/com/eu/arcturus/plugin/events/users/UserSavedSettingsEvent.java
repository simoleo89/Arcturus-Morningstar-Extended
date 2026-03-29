package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserSavedSettingsEvent extends UserEvent {

    public UserSavedSettingsEvent(Habbo habbo) {
        super(habbo);
    }
}
