package com.eu.arcturus.messages.incoming.guardians;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class GuardianAcceptRequestEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Emulator.getGameEnvironment().getGuideManager().acceptTicket(this.client.getHabbo(), this.packet.readBoolean());
    }
}
