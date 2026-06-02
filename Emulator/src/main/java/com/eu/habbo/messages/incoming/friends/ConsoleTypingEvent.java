package com.eu.habbo.messages.incoming.friends;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.friends.FriendTypingComposer;

public class ConsoleTypingEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int peerId = this.packet.readInt();
        boolean isTyping = this.packet.readBoolean();
        Habbo me = this.client.getHabbo();

        if (me == null || peerId <= 0) return;

        if (me.getMessenger().getFriend(peerId) == null) return;

        Habbo peer = Emulator.getGameServer().getGameClientManager().getHabbo(peerId);
        if (peer == null || peer.getClient() == null) return;

        peer.getClient().sendResponse(new FriendTypingComposer(me.getHabboInfo().getId(), isTyping));
    }
}
