package com.eu.arcturus.messages.incoming.trading;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTrade;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class TradeCancelEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Habbo habbo = this.client.getHabbo();
        Room room = habbo.getHabboInfo().getCurrentRoom();

        if (room == null)
            return;

        RoomTrade trade = room.getActiveTradeForHabbo(habbo);

        if (trade == null)
            return;

        trade.stopTrade(habbo);
        room.stopTrade(trade);
    }
}
