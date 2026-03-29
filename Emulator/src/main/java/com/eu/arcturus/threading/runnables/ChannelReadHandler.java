package com.eu.arcturus.threading.runnables;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.messages.ClientMessage;
import com.eu.arcturus.networking.gameserver.GameServerAttributes;
import io.netty.channel.ChannelHandlerContext;

public class ChannelReadHandler implements Runnable {

    private final ChannelHandlerContext ctx;
    private final ClientMessage message;

    public ChannelReadHandler(ChannelHandlerContext ctx, ClientMessage message) {
        this.ctx = ctx;
        this.message = message;
    }

    public void run() {
        try {
            GameClient client = this.ctx.channel().attr(GameServerAttributes.CLIENT).get();

            if (client != null) {
                Emulator.getGameServer().getPacketManager().handlePacket(client, message);
            }
        } finally {
            this.message.release();
        }
    }
}
