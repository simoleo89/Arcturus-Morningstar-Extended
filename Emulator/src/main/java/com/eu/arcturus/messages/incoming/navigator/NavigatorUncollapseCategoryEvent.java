package com.eu.arcturus.messages.incoming.navigator;

import com.eu.arcturus.habbohotel.navigation.DisplayMode;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class NavigatorUncollapseCategoryEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String category = this.packet.readString();
        this.client.getHabbo().getHabboStats().navigatorWindowSettings.setDisplayMode(category, DisplayMode.VISIBLE);
    }
}
