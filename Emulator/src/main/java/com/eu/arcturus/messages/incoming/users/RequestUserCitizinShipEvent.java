package com.eu.arcturus.messages.incoming.users;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.users.UserCitizinShipComposer;

public class RequestUserCitizinShipEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new UserCitizinShipComposer(this.packet.readString()));
    }
}
