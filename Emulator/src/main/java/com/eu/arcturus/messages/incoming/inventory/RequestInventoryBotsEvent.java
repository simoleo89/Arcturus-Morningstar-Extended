package com.eu.arcturus.messages.incoming.inventory;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.inventory.InventoryBotsComposer;

public class RequestInventoryBotsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new InventoryBotsComposer(this.client.getHabbo()));
    }
}
