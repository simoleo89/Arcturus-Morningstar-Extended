package com.eu.arcturus.plugin.events.inventory;

import com.eu.arcturus.habbohotel.users.HabboInventory;
import com.eu.arcturus.habbohotel.users.HabboItem;

public class InventoryItemAddedEvent extends InventoryItemEvent {
    public InventoryItemAddedEvent(HabboInventory inventory, HabboItem item) {
        super(inventory, item);
    }
}
