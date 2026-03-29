package com.eu.arcturus.plugin.events.users.friends;

import com.eu.arcturus.habbohotel.messenger.MessengerBuddy;
import com.eu.arcturus.habbohotel.users.Habbo;

public class UserRelationShipEvent extends UserFriendEvent {
    public int relationShip;


    public UserRelationShipEvent(Habbo habbo, MessengerBuddy friend, int relationShip) {
        super(habbo, friend);

        this.relationShip = relationShip;
    }
}