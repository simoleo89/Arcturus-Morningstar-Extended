package com.eu.arcturus.plugin.events.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;

public class RoomLoadedEvent extends RoomEvent {

    public RoomLoadedEvent(Room room) {
        super(room);
    }
}