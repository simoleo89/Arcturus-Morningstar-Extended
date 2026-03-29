package com.eu.arcturus.plugin.events.users.friends;

import com.eu.arcturus.habbohotel.messenger.MessengerBuddy;
import com.eu.arcturus.habbohotel.users.Habbo;

public class UserFriendChatEvent extends UserFriendEvent {

    public String message;


    public UserFriendChatEvent(Habbo habbo, MessengerBuddy friend, String message) {
        super(habbo, friend);

        this.message = message;
    }
}
