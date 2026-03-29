package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;

public class UserRolledEvent extends UserEvent {

    public final HabboItem roller;


    public final RoomTile location;


    public UserRolledEvent(Habbo habbo, HabboItem roller, RoomTile location) {
        super(habbo);

        this.roller = roller;
        this.location = location;
    }
}
