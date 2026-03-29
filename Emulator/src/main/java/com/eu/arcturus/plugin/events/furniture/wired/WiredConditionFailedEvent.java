package com.eu.arcturus.plugin.events.furniture.wired;

import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredCondition;
import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredTrigger;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.plugin.events.roomunit.RoomUnitEvent;

public class WiredConditionFailedEvent extends RoomUnitEvent {

    public final InteractionWiredTrigger trigger;


    public final InteractionWiredCondition condition;


    public WiredConditionFailedEvent(Room room, RoomUnit roomUnit, InteractionWiredTrigger trigger, InteractionWiredCondition condition) {
        super(room, roomUnit);
        this.trigger = trigger;
        this.condition = condition;
    }
}
