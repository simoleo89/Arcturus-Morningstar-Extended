package com.eu.arcturus.messages.incoming.inventory;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.inventory.InventoryPetsComposer;

public class RequestInventoryPetsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new InventoryPetsComposer(this.client.getHabbo()));
    }
}
