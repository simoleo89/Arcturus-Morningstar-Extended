package com.eu.arcturus.plugin.events.furniture;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;

public class FurnitureRotatedEvent extends FurnitureUserEvent {

    public final int oldRotation;


    public FurnitureRotatedEvent(HabboItem furniture, Habbo habbo, int oldRotation) {
        super(furniture, habbo);

        this.oldRotation = oldRotation;
    }
}
