package com.eu.arcturus.messages.outgoing.rooms.items;

import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class WallItemUpdateComposer extends MessageComposer {
    private final HabboItem item;

    public WallItemUpdateComposer(HabboItem item) {
        this.item = item;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.WallItemUpdateComposer);
        this.item.serializeWallData(this.response);
        this.response.appendString(this.item.getUserId() + "");
        return this.response;
    }

    public HabboItem getItem() {
        return item;
    }
}
