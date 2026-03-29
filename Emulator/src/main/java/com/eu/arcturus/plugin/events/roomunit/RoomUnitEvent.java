package com.eu.arcturus.plugin.events.roomunit;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.plugin.Event;

public abstract class RoomUnitEvent extends Event {

    public final Room room;


    public final RoomUnit roomUnit;


    public RoomUnitEvent(Room room, RoomUnit roomUnit) {
        this.room = room;
        this.roomUnit = roomUnit;
    }
}
