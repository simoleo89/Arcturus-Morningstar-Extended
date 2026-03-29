package com.eu.arcturus.messages.outgoing.trading;

import com.eu.arcturus.habbohotel.rooms.RoomTradeUser;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class TradeAcceptedComposer extends MessageComposer {
    private final RoomTradeUser tradeUser;

    public TradeAcceptedComposer(RoomTradeUser tradeUser) {
        this.tradeUser = tradeUser;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.TradeAcceptedComposer);
        this.response.appendInt(this.tradeUser.getUserId());
        this.response.appendInt(this.tradeUser.getAccepted());
        return this.response;
    }

    public RoomTradeUser getTradeUser() {
        return tradeUser;
    }
}
