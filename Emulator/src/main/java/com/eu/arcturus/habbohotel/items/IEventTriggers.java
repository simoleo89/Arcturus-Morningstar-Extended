package com.eu.arcturus.habbohotel.items;

import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;

public interface IEventTriggers {
    void onClick(GameClient client, Room room, Object[] objects) throws Exception;

    void onWalkOn(RoomUnit roomUnit, Room room, Object[] objects) throws Exception;

    void onWalkOff(RoomUnit roomUnit, Room room, Object[] objects) throws Exception;
}
