package com.eu.arcturus.messages.outgoing.rooms.items;

import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class UpdateStackHeightTileHeightComposer extends MessageComposer {
    private final HabboItem item;
    private final int height;

    public UpdateStackHeightTileHeightComposer(HabboItem item, int height) {
        this.item = item;
        this.height = height;

    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UpdateStackHeightTileHeightComposer);
        this.response.appendInt(this.item.getId());
        this.response.appendInt(this.height);
        return this.response;
    }

    public HabboItem getItem() {
        return item;
    }

    public int getHeight() {
        return height;
    }
}
