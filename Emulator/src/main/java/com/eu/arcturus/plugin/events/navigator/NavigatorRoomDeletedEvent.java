package com.eu.arcturus.plugin.events.navigator;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.events.users.UserEvent;

public class NavigatorRoomDeletedEvent extends UserEvent {
    public final Room room;

    public NavigatorRoomDeletedEvent(Habbo habbo, Room room) {
        super(habbo);

        this.room = room;
    }
}
