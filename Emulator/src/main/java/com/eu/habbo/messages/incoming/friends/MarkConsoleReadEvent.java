package com.eu.habbo.messages.incoming.friends;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.friends.ConsoleReadReceiptComposer;

public class MarkConsoleReadEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int peerId = this.packet.readInt();
        Habbo me = this.client.getHabbo();

        if (me == null || peerId <= 0) return;

        if (me.getMessenger().getFriend(peerId) == null) return;

        Habbo peer = Emulator.getGameServer().getGameClientManager().getHabbo(peerId);
        if (peer == null || peer.getClient() == null) return;

        peer.getClient().sendResponse(new ConsoleReadReceiptComposer(me.getHabboInfo().getId()));
    }
}
