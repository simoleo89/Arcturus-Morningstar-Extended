package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class UnknownFurniModelComposer extends MessageComposer {
    private final HabboItem item;
    private final int unknownInt;

    public UnknownFurniModelComposer(HabboItem item, int unknownInt) {
        this.item = item;
        this.unknownInt = unknownInt;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UnknownFurniModelComposer);
        this.response.appendInt(this.item.getId());
        this.response.appendInt(this.unknownInt);
        return this.response;
    }

    public HabboItem getItem() {
        return item;
    }

    public int getUnknownInt() {
        return unknownInt;
    }
}