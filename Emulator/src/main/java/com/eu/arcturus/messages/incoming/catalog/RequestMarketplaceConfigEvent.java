package com.eu.arcturus.messages.incoming.catalog;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.marketplace.MarketplaceConfigComposer;

public class RequestMarketplaceConfigEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new MarketplaceConfigComposer());
    }
}
