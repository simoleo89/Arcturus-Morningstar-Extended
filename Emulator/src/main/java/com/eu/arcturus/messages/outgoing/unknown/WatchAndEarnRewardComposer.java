package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class WatchAndEarnRewardComposer extends MessageComposer {
    private final Item item;

    public WatchAndEarnRewardComposer(Item item) {
        this.item = item;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.WatchAndEarnRewardComposer);
        this.response.appendString(this.item.getType().code);
        this.response.appendInt(this.item.getId());
        this.response.appendString(this.item.getName());
        this.response.appendString(this.item.getFullName());
        return this.response;
    }

    public Item getItem() {
        return item;
    }
}