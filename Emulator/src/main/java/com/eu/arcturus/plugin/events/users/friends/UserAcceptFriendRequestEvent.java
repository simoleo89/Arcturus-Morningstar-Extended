package com.eu.arcturus.plugin.events.users.friends;

import com.eu.arcturus.habbohotel.messenger.MessengerBuddy;
import com.eu.arcturus.habbohotel.users.Habbo;

public class UserAcceptFriendRequestEvent extends UserFriendEvent {

    public UserAcceptFriendRequestEvent(Habbo habbo, MessengerBuddy friend) {
        super(habbo, friend);
    }
}
