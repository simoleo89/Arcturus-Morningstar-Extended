package com.eu.arcturus.messages.outgoing.rooms.items;

import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RemoveWallItemComposer extends MessageComposer {
    private final HabboItem item;

    public RemoveWallItemComposer(HabboItem item) {
        this.item = item;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RemoveWallItemComposer);
        this.response.appendString(this.item.getId() + "");
        this.response.appendInt(this.item.getUserId());
        return this.response;
    }

    public HabboItem getItem() {
        return item;
    }
}
