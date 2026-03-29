package com.eu.arcturus.messages.incoming.friends;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.friends.FriendsComposer;
import com.eu.arcturus.messages.outgoing.friends.MessengerInitComposer;

import java.util.ArrayList;

public class RequestInitFriendsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        ArrayList<ServerMessage> messages = new ArrayList<>();
        messages.add(new MessengerInitComposer(this.client.getHabbo()).compose());
        messages.addAll(FriendsComposer.getMessagesForBuddyList(this.client.getHabbo().getMessenger().getFriends().values()));
        this.client.sendResponses(messages);
    }
}
