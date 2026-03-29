package com.eu.arcturus.messages.incoming.hotelview;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.hotelview.BonusRareComposer;

public class HotelViewRequestBonusRareEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new BonusRareComposer(this.client.getHabbo()));
    }
}
