package com.eu.arcturus.plugin.events.furniture;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;

public abstract class FurnitureUserEvent extends FurnitureEvent {

    public final Habbo habbo;


    public FurnitureUserEvent(HabboItem furniture, Habbo habbo) {
        super(furniture);
        this.habbo = habbo;
    }
}
