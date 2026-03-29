package com.eu.arcturus.messages.incoming.users;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.users.UserWardrobeComposer;

public class RequestUserWardrobeEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new UserWardrobeComposer(this.client.getHabbo().getInventory().getWardrobeComponent()));
    }
}
