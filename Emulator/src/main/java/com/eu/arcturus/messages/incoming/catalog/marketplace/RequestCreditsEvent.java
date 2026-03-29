package com.eu.arcturus.messages.incoming.catalog.marketplace;

import com.eu.arcturus.habbohotel.catalog.marketplace.MarketPlace;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class RequestCreditsEvent extends MessageHandler {
    @Override
    public int getRatelimit() {
        return 2000;
    }

    @Override
    public void handle() throws Exception {
        MarketPlace.getCredits(this.client);
    }
}
