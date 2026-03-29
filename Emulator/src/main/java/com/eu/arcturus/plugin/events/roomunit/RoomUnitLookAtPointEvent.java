package com.eu.arcturus.plugin.events.roomunit;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;

public class RoomUnitLookAtPointEvent extends RoomUnitEvent {

    public final RoomTile location;


    public RoomUnitLookAtPointEvent(Room room, RoomUnit roomUnit, RoomTile location) {
        super(room, roomUnit);

        this.location = location;
    }
}
