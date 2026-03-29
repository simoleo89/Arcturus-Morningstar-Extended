package com.eu.arcturus.plugin.events.furniture;

import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.habbohotel.users.HabboItem;

public class FurnitureRolledEvent extends FurnitureEvent {

    public final HabboItem roller;


    public final RoomTile newLocation;


    public FurnitureRolledEvent(HabboItem furniture, HabboItem roller, RoomTile newLocation) {
        super(furniture);

        this.roller = roller;
        this.newLocation = newLocation;
    }
}
