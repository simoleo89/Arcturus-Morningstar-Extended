package com.eu.arcturus.messages.incoming.handshake;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.handshake.PongComposer;

public class PingEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new PongComposer(this.packet.readInt()));
    }
}
