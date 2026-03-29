package com.eu.arcturus.messages.outgoing.rooms.items.rentablespaces;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RentableSpaceUnknown2Composer extends MessageComposer {
    private final int itemId;

    public RentableSpaceUnknown2Composer(int itemId) {
        this.itemId = itemId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RentableSpaceUnknown2Composer);
        this.response.appendInt(this.itemId);
        return this.response;
    }

    public int getItemId() {
        return itemId;
    }
}
