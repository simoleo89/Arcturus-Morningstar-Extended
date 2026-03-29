package com.eu.arcturus.messages.incoming.users;

import com.eu.arcturus.messages.incoming.MessageHandler;

public class UpdateUIFlagsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int flags = this.packet.readInt();

        this.client.getHabbo().getHabboStats().uiFlags = flags;
        this.client.getHabbo().getHabboStats().run();
    }
}
