package com.eu.arcturus.messages.incoming.handshake;

import com.eu.arcturus.messages.NoAuthMessage;
import com.eu.arcturus.messages.incoming.MessageHandler;

@NoAuthMessage
public class ReleaseVersionEvent extends MessageHandler {

    @Override
    public void handle() throws Exception {
        this.packet.readString();
    }
}
