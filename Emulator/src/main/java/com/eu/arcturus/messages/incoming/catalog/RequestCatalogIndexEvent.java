package com.eu.arcturus.messages.incoming.catalog;

import com.eu.arcturus.messages.incoming.MessageHandler;

public class RequestCatalogIndexEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        //this.client.sendResponse(new CatalogPagesListComposer(this.client.getHabbo(), "NORMAL"));
    }
}
