package com.eu.arcturus.messages.incoming.helper;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.modtool.ModToolSanctionInfoComposer;

public class MySanctionStatusEvent extends MessageHandler {

    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new ModToolSanctionInfoComposer(this.client.getHabbo()));
    }
}
