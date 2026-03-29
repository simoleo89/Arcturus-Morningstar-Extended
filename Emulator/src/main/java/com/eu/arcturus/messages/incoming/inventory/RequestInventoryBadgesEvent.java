package com.eu.arcturus.messages.incoming.inventory;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.inventory.InventoryBadgesComposer;

public class RequestInventoryBadgesEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new InventoryBadgesComposer(this.client.getHabbo()));
    }
}
