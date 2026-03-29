package com.eu.arcturus.plugin.events.marketplace;

import com.eu.arcturus.habbohotel.catalog.marketplace.MarketPlaceOffer;

public class MarketPlaceItemCancelledEvent extends MarketPlaceEvent {
    public final MarketPlaceOffer offer;

    public MarketPlaceItemCancelledEvent(MarketPlaceOffer offer) {
        this.offer = offer;
    }
}
