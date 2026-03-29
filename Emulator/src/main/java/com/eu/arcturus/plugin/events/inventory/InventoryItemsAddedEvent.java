package com.eu.arcturus.plugin.events.inventory;

import com.eu.arcturus.habbohotel.users.HabboInventory;
import com.eu.arcturus.habbohotel.users.HabboItem;
import java.util.HashSet;

public class InventoryItemsAddedEvent extends InventoryEvent {
    public final HashSet<HabboItem> items;

    public InventoryItemsAddedEvent(HabboInventory inventory, HashSet<HabboItem> items) {
        super(inventory);
        this.items = items;
    }
}
