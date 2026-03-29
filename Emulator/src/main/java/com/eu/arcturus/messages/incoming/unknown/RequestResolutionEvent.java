package com.eu.arcturus.messages.incoming.unknown;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.events.resolution.NewYearResolutionComposer;

public class RequestResolutionEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.packet.readInt(); // itemId - not used
        int viewAll = this.packet.readInt();

        if (viewAll == 0) {
            this.client.sendResponse(new NewYearResolutionComposer());
        }
    }
}
