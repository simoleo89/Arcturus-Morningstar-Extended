package com.eu.habbo.messages.incoming.inventory;

import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.inventory.InventoryItemsComposer;

import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

public class RequestInventoryItemsEvent extends MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestInventoryItemsEvent.class);

    @Override
    public int getRatelimit() {
        return 500;
    }

    @Override
    public void handle() throws Exception {
        int totalItems = this.client.getHabbo().getInventory().getItemsComponent().getItems().size();

        if (totalItems == 0) {
                this.client.sendResponse(new InventoryItemsComposer(0, 1, new HashMap<>()));
                return;
            }
            
        int totalFragments = (int) Math.ceil((double) totalItems / 1000.0);

        if (totalFragments == 0) {
            totalFragments = 1;
        }

        synchronized (this.client.getHabbo().getInventory().getItemsComponent().getItems()) {
            Map<Integer, HabboItem> items = new HashMap<>();

            int count = 0;
            int fragmentNumber = 0;

            for (Map.Entry<Integer, HabboItem> entry : this.client.getHabbo().getInventory().getItemsComponent().getItems().entrySet()) {

                if (count == 0) {
                    fragmentNumber++;
                }

                items.put(entry.getKey(), entry.getValue());
                count++;

                if (count == 1000) {
                    this.client.sendResponse(new InventoryItemsComposer(fragmentNumber, totalFragments, items));
                    count = 0;
                    items.clear();
                }
            }

            if(count > 0 && !items.isEmpty()) this.client.sendResponse(new InventoryItemsComposer(fragmentNumber, totalFragments, items));
        }
    }
}
