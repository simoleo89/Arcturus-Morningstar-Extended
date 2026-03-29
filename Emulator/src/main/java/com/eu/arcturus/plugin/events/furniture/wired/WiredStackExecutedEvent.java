package com.eu.arcturus.plugin.events.furniture.wired;

import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredCondition;
import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredEffect;
import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredTrigger;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.plugin.events.roomunit.RoomUnitEvent;
import java.util.HashSet;

public class WiredStackExecutedEvent extends RoomUnitEvent {

    public final InteractionWiredTrigger trigger;


    public final HashSet<InteractionWiredEffect> effects;


    public final HashSet<InteractionWiredCondition> conditions;


    public WiredStackExecutedEvent(Room room, RoomUnit roomUnit, InteractionWiredTrigger trigger, HashSet<InteractionWiredEffect> effects, HashSet<InteractionWiredCondition> conditions) {
        super(room, roomUnit);

        this.trigger = trigger;
        this.effects = effects;
        this.conditions = conditions;
    }
}
