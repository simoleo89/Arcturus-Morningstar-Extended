package com.eu.arcturus.plugin.events.inventory;

import com.eu.arcturus.habbohotel.users.HabboInventory;
import com.eu.arcturus.habbohotel.users.HabboItem;

public class InventoryItemEvent extends InventoryEvent {
    public HabboItem item;

    public InventoryItemEvent(HabboInventory inventory, HabboItem item) {
        super(inventory);

        this.item = item;
    }
}
