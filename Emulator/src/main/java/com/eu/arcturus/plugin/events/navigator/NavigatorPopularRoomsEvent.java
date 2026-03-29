package com.eu.arcturus.plugin.events.navigator;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;

import java.util.ArrayList;

public class NavigatorPopularRoomsEvent extends NavigatorRoomsEvent {

    public final ArrayList<Room> rooms;


    public NavigatorPopularRoomsEvent(Habbo habbo, ArrayList<Room> rooms) {
        super(habbo, rooms);

        this.rooms = rooms;
    }
}
