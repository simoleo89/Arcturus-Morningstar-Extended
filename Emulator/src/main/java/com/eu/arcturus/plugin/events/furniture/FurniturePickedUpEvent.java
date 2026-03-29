package com.eu.arcturus.plugin.events.furniture;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;

public class FurniturePickedUpEvent extends FurnitureUserEvent {

    public FurniturePickedUpEvent(HabboItem furniture, Habbo habbo) {
        super(furniture, habbo);
    }
}
