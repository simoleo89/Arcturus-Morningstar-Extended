package com.eu.arcturus.messages.outgoing.trading;

import com.eu.arcturus.habbohotel.rooms.RoomTrade;
import com.eu.arcturus.habbohotel.rooms.RoomTradeUser;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class TradeStartComposer extends MessageComposer {
    private final RoomTrade roomTrade;
    private final int state;

    public TradeStartComposer(RoomTrade roomTrade) {
        this.roomTrade = roomTrade;
        this.state = 1;
    }

    public TradeStartComposer(RoomTrade roomTrade, int state) {
        this.roomTrade = roomTrade;
        this.state = state;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.TradeStartComposer);
        for (RoomTradeUser tradeUser : this.roomTrade.getRoomTradeUsers()) {
            this.response.appendInt(tradeUser.getHabbo().getHabboInfo().getId());
            this.response.appendInt(this.state);
        }
        return this.response;
    }

    public RoomTrade getRoomTrade() {
        return roomTrade;
    }

    public int getState() {
        return state;
    }
}
