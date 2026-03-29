package com.eu.arcturus.plugin.events.trading;

import com.eu.arcturus.habbohotel.rooms.RoomTradeUser;
import com.eu.arcturus.plugin.Event;

public class TradeConfirmEvent extends Event {
    public final RoomTradeUser userOne;
    public final RoomTradeUser userTwo;

    public TradeConfirmEvent(RoomTradeUser userOne, RoomTradeUser userTwo) {
        this.userOne = userOne;
        this.userTwo = userTwo;
    }
}
