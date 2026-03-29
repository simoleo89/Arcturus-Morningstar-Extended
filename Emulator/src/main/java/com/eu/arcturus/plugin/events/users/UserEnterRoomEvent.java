package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;

public class UserEnterRoomEvent extends UserEvent {

    public final Room room;


    public UserEnterRoomEvent(Habbo habbo, Room room) {
        super(habbo);

        this.room = room;
    }
}
