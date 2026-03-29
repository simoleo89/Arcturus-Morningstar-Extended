package com.eu.arcturus.messages.incoming.catalog.marketplace;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.marketplace.MarketplaceItemInfoComposer;

public class RequestItemInfoEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.packet.readInt();
        int id = this.packet.readInt();

        this.client.sendResponse(new MarketplaceItemInfoComposer(id));
    }
}
