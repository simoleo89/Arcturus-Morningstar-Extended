package com.eu.arcturus.plugin.events.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;

public class RoomUnloadingEvent extends RoomEvent {

    public RoomUnloadingEvent(Room room) {
        super(room);
    }
}