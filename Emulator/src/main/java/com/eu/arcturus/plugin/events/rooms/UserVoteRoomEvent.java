package com.eu.arcturus.plugin.events.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.Event;

public class UserVoteRoomEvent extends Event {
    public final Room room;
    public final Habbo habbo;

    public UserVoteRoomEvent(Room room, Habbo habbo) {
        this.room = room;
        this.habbo = habbo;
    }
}
