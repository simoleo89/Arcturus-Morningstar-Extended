package com.eu.arcturus.messages.incoming.catalog.recycler;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.RecyclerLogicComposer;

public class RequestRecyclerLogicEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new RecyclerLogicComposer());
    }
}
