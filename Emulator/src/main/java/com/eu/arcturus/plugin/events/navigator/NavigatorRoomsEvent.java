package com.eu.arcturus.plugin.events.navigator;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.events.users.UserEvent;

import java.util.ArrayList;

public abstract class NavigatorRoomsEvent extends UserEvent {

    public final ArrayList<Room> rooms;


    public NavigatorRoomsEvent(Habbo habbo, ArrayList<Room> rooms) {
        super(habbo);

        this.rooms = rooms;
    }
}
