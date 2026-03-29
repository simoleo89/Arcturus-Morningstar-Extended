package com.eu.arcturus.plugin.events.marketplace;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;

public class MarketPlaceItemOfferedEvent extends MarketPlaceEvent {
    public final Habbo habbo;
    public final HabboItem item;
    public int price;

    public MarketPlaceItemOfferedEvent(Habbo habbo, HabboItem item, int price) {
        this.habbo = habbo;
        this.item = item;
        this.price = price;
    }
}
