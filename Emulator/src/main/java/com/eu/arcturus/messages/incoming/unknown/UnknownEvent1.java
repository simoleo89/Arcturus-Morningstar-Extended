package com.eu.arcturus.messages.incoming.unknown;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.unknown.IgnoredUsersComposer;

public class UnknownEvent1 extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new IgnoredUsersComposer());
    }
}
