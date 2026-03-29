package com.eu.arcturus.messages.outgoing.wired;

import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class WiredOpenComposer extends MessageComposer {
    private final HabboItem item;

    public WiredOpenComposer(HabboItem item) {
        this.item = item;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.WiredOpenComposer);
        this.response.appendInt(this.item.getId());
        return this.response;
    }

    public HabboItem getItem() {
        return item;
    }
}