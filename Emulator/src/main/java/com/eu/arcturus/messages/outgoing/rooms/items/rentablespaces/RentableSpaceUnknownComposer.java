package com.eu.arcturus.messages.outgoing.rooms.items.rentablespaces;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RentableSpaceUnknownComposer extends MessageComposer {
    private final int itemId;

    public RentableSpaceUnknownComposer(int itemId) {
        this.itemId = itemId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RentableSpaceUnknownComposer);
        this.response.appendInt(this.itemId);
        return this.response;
    }

    public int getItemId() {
        return itemId;
    }
}
