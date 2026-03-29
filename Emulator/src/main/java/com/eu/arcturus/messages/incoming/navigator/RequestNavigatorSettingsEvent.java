package com.eu.arcturus.messages.incoming.navigator;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.navigator.NewNavigatorSettingsComposer;

public class RequestNavigatorSettingsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {


        this.client.sendResponse(new NewNavigatorSettingsComposer(this.client.getHabbo().getHabboStats().navigatorWindowSettings));

    }
}
