package com.eu.arcturus.messages.outgoing.inventory;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RemoveHabboItemComposer extends MessageComposer {
    private final int itemId;

    public RemoveHabboItemComposer(final int itemId) {
        this.itemId = itemId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RemoveHabboItemComposer);
        this.response.appendInt(this.itemId);
        return this.response;
    }

    public int getItemId() {
        return itemId;
    }
}
