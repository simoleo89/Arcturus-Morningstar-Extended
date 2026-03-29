package com.eu.arcturus.messages.incoming.trading;

import com.eu.arcturus.habbohotel.rooms.RoomTrade;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class TradeConfirmEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Habbo habbo = this.client.getHabbo();
        if (habbo == null || habbo.getHabboInfo().getCurrentRoom() == null) return;

        RoomTrade trade = habbo.getHabboInfo().getCurrentRoom().getActiveTradeForHabbo(habbo);

        if (trade == null || !trade.getRoomTradeUserForHabbo(habbo).getAccepted())
            return;

        trade.confirm(habbo);
    }
}
