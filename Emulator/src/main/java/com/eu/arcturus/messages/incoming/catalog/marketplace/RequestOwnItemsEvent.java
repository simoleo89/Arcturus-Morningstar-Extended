package com.eu.arcturus.messages.incoming.catalog.marketplace;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.marketplace.MarketplaceOwnItemsComposer;

public class RequestOwnItemsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new MarketplaceOwnItemsComposer(this.client.getHabbo()));
    }
}
