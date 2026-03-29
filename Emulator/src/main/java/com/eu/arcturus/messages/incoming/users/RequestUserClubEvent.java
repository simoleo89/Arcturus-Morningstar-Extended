package com.eu.arcturus.messages.incoming.users;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.users.UserClubComposer;

public class RequestUserClubEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String subscriptionType = this.packet.readString();
        this.client.sendResponse(new UserClubComposer(this.client.getHabbo(), subscriptionType));
    }
}
