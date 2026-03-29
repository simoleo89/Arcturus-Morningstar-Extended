package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserPointsEvent extends UserEvent {

    public int points;


    public int type;


    public UserPointsEvent(Habbo habbo, int points, int type) {
        super(habbo);

        this.points = points;
        this.type = type;
    }
}