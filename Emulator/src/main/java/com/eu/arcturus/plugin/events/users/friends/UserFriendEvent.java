package com.eu.arcturus.plugin.events.users.friends;

import com.eu.arcturus.habbohotel.messenger.MessengerBuddy;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.events.users.UserEvent;

public abstract class UserFriendEvent extends UserEvent {
    public final MessengerBuddy friend;


    public UserFriendEvent(Habbo habbo, MessengerBuddy friend) {
        super(habbo);

        this.friend = friend;
    }
}
