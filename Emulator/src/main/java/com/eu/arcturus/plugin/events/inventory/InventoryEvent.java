package com.eu.arcturus.plugin.events.inventory;

import com.eu.arcturus.habbohotel.users.HabboInventory;
import com.eu.arcturus.plugin.Event;

public abstract class InventoryEvent extends Event {
    public final HabboInventory inventory;

    protected InventoryEvent(HabboInventory inventory) {
        this.inventory = inventory;
    }
}
