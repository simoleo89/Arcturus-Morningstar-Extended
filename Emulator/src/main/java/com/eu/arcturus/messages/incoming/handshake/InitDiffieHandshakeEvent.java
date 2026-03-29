package com.eu.arcturus.messages.incoming.handshake;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.messages.NoAuthMessage;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.handshake.InitDiffieHandshakeComposer;

@NoAuthMessage
public class InitDiffieHandshakeEvent extends MessageHandler {

    @Override
    public void handle() throws Exception {
        if (this.client.getEncryption() == null) {
            Emulator.getGameServer().getGameClientManager().disposeClient(this.client);
            return;
        }

        this.client.sendResponse(new InitDiffieHandshakeComposer(
                this.client.getEncryption().getDiffie().getSignedPrime(),
                this.client.getEncryption().getDiffie().getSignedGenerator()));
    }

}
