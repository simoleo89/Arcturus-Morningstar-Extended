package com.eu.arcturus.messages.incoming.handshake;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.crypto.HabboRC4;
import com.eu.arcturus.messages.NoAuthMessage;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.handshake.CompleteDiffieHandshakeComposer;
import com.eu.arcturus.networking.gameserver.decoders.GameByteDecryption;
import com.eu.arcturus.networking.gameserver.encoders.GameByteEncryption;
import com.eu.arcturus.networking.gameserver.GameServerAttributes;

@NoAuthMessage
public class CompleteDiffieHandshakeEvent extends MessageHandler {

    @Override
    public void handle() throws Exception {
        if (this.client.getEncryption() == null) {
            Emulator.getGameServer().getGameClientManager().disposeClient(this.client);
            return;
        }

        byte[] sharedKey = this.client.getEncryption().getDiffie().getSharedKey(this.packet.readString());

        this.client.setHandshakeFinished(true);
        this.client.sendResponse(new CompleteDiffieHandshakeComposer(this.client.getEncryption().getDiffie().getPublicKey()));

        this.client.getChannel().attr(GameServerAttributes.CRYPTO_CLIENT).set(new HabboRC4(sharedKey));
        this.client.getChannel().attr(GameServerAttributes.CRYPTO_SERVER).set(new HabboRC4(sharedKey));

        this.client.getChannel().pipeline().addFirst(new GameByteDecryption());
        this.client.getChannel().pipeline().addFirst(new GameByteEncryption());
    }

}
