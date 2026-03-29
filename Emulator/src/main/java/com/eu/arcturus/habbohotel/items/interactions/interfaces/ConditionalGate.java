package com.eu.arcturus.habbohotel.items.interactions.interfaces;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;

public interface ConditionalGate {
    public void onRejected(RoomUnit roomUnit, Room room, Object[] objects);
}
