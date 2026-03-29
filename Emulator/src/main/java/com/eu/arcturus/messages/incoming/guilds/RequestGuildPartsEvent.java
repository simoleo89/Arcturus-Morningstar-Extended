package com.eu.arcturus.messages.incoming.guilds;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.guilds.GuildPartsComposer;

public class RequestGuildPartsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new GuildPartsComposer());
    }
}
