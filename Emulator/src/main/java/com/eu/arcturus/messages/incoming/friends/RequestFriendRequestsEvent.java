package com.eu.arcturus.messages.incoming.friends;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.friends.LoadFriendRequestsComposer;

public class RequestFriendRequestsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new LoadFriendRequestsComposer(this.client.getHabbo()));
    }
}
