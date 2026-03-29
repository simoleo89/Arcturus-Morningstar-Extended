package com.eu.arcturus.plugin.events.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.plugin.Event;

public abstract class RoomEvent extends Event {

    public final Room room;


    public RoomEvent(Room room) {
        this.room = room;
    }
}