package com.eu.arcturus.plugin.events.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;

public class RoomUncachedEvent extends RoomEvent {

    public RoomUncachedEvent(Room room) {
        super(room);
    }
}