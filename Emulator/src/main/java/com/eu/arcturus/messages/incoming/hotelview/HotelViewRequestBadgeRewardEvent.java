package com.eu.arcturus.messages.incoming.hotelview;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.hotelview.HotelViewBadgeButtonConfigComposer;

public class HotelViewRequestBadgeRewardEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String badge = this.packet.readString();
        this.client.sendResponse(new HotelViewBadgeButtonConfigComposer(Emulator.getConfig().getValue("hotelview.badgereward." + badge + ".badge"), Emulator.getConfig().getBoolean("hotelview.badgereward." + badge + ".enabled")));
    }
}