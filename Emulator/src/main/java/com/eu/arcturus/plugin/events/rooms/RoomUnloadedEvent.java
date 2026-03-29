package com.eu.arcturus.plugin.events.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;

public class RoomUnloadedEvent extends RoomEvent {

    public RoomUnloadedEvent(Room room) {
        super(room);
    }
}