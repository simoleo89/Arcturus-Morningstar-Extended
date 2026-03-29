package com.eu.arcturus.messages.incoming.catalog;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.GiftConfigurationComposer;

public class RequestGiftConfigurationEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new GiftConfigurationComposer());
    }
}
