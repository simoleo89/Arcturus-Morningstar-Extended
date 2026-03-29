package com.eu.arcturus.messages.incoming.inventory;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.inventory.InventoryRefreshComposer;
import com.eu.arcturus.messages.outgoing.inventory.RemoveHabboItemComposer;
import com.eu.arcturus.threading.runnables.QueryDeleteHabboItems;

import java.util.HashMap;

public class RequestInventoryItemsDelete extends MessageHandler {
    public int getRatelimit() {
        return 500;
    }

    public void handle() {
        int itemId = this.packet.readInt();
        int amount = this.packet.readInt();
        HabboItem habboItem = this.client.getHabbo().getInventory().getItemsComponent().getHabboItem(itemId);
        if (habboItem == null)
            return;
        Item item = habboItem.getBaseItem();
        if (item == null)
            return;
        if (!hasFurnitureInInventory(this.client.getHabbo(), item, Math.abs(amount)))
            return;
        final Habbo habbo = this.client.getHabbo();
        if (habbo == null)
            return;
        HashMap<Integer, HabboItem> toRemove = new HashMap<>();
        for (int i = 0; i < Math.abs(amount); i++) {
            HabboItem habboInventoryItem = habbo.getInventory().getItemsComponent().getAndRemoveHabboItem(item);
            if (habboInventoryItem != null)
                toRemove.put(habboInventoryItem.getId(), habboInventoryItem);
        }
        toRemove.values().forEach(object -> {
            habbo.getClient().sendResponse(new RemoveHabboItemComposer(object.getGiftAdjustedId()));
            return true;
        });
        habbo.getClient().sendResponse(new InventoryRefreshComposer());
        Emulator.getThreading().run(new QueryDeleteHabboItems(toRemove));
    }

    private boolean hasFurnitureInInventory(Habbo habbo, Item item, Integer amount) {
        int count = 0;
        for (Iterator<HabboItem> tObjectHashIterator = habbo.getInventory().getItemsComponent().getItemsAsValueCollection().iterator(); tObjectHashIterator.hasNext(); ) {
            HabboItem habboItem = tObjectHashIterator.next();
            if (habboItem.getBaseItem().getId() == item.getId())
                count++;
        }
        return count >= amount;
    }
}