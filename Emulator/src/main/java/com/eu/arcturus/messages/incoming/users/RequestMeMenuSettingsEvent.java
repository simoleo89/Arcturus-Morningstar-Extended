package com.eu.arcturus.messages.incoming.users;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.users.MeMenuSettingsComposer;

public class RequestMeMenuSettingsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new MeMenuSettingsComposer(this.client.getHabbo()));
    }
}
