package com.eu.arcturus.plugin.events.roomunit;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;

public class RoomUnitSetGoalEvent extends RoomUnitEvent {

    public final RoomTile goal;

    public RoomUnitSetGoalEvent(Room room, RoomUnit roomUnit, RoomTile goal) {
        super(room, roomUnit);

        this.goal = goal;
    }


    public void setGoal(RoomTile t) {
        super.roomUnit.setGoalLocation(t);
    }
}
