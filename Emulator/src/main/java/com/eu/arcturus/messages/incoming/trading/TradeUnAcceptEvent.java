package com.eu.arcturus.messages.incoming.trading;

import com.eu.arcturus.habbohotel.rooms.RoomTrade;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class TradeUnAcceptEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Habbo habbo = this.client.getHabbo();
        RoomTrade trade = habbo.getHabboInfo().getCurrentRoom().getActiveTradeForHabbo(habbo);

        if (trade == null)
            return;

        trade.accept(habbo, false);
    }
}
