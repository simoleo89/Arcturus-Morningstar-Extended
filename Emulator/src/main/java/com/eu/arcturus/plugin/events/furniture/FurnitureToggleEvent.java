package com.eu.arcturus.plugin.events.furniture;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;

public class FurnitureToggleEvent extends FurnitureUserEvent {
    public int state;

    public FurnitureToggleEvent(HabboItem furniture, Habbo habbo, int state) {
        super(furniture, habbo);

        this.state = state;
    }
}
