package com.eu.arcturus.messages.outgoing.rooms.items;

import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class PostItStickyPoleOpenComposer extends MessageComposer {
    private final HabboItem item;

    public PostItStickyPoleOpenComposer(HabboItem item) {
        this.item = item;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.PostItStickyPoleOpenComposer);
        this.response.appendInt(this.item == null ? -1234 : this.item.getId());
        this.response.appendString("");
        return this.response;
    }

    public HabboItem getItem() {
        return item;
    }
}
