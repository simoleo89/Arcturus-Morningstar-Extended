package com.eu.arcturus.plugin.events.furniture;

import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.plugin.Event;

public abstract class FurnitureEvent extends Event {

    public final HabboItem furniture;


    public FurnitureEvent(HabboItem furniture) {
        this.furniture = furniture;
    }
}
