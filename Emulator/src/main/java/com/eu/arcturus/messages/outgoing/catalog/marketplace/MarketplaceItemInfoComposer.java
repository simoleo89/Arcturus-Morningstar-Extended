package com.eu.arcturus.messages.outgoing.catalog.marketplace;

import com.eu.arcturus.habbohotel.catalog.marketplace.MarketPlace;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class MarketplaceItemInfoComposer extends MessageComposer {
    private final int itemId;

    public MarketplaceItemInfoComposer(int itemId) {
        this.itemId = itemId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.MarketplaceItemInfoComposer);
        MarketPlace.serializeItemInfo(this.itemId, this.response);
        return this.response;
    }

    public int getItemId() {
        return itemId;
    }
}
