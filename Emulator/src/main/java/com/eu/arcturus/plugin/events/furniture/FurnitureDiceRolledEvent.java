package com.eu.arcturus.plugin.events.furniture;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;

public class FurnitureDiceRolledEvent extends FurnitureUserEvent {

    public int result;


    public FurnitureDiceRolledEvent(HabboItem furniture, Habbo habbo, int result) {
        super(furniture, habbo);

        this.result = result;
    }
}
