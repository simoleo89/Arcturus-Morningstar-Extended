package com.eu.arcturus.messages.incoming.catalog;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.DiscountComposer;

public class RequestDiscountEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new DiscountComposer());
    }
}
