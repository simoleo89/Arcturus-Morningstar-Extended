package com.eu.arcturus.messages.outgoing.rooms.users;

import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomUserReceivedHandItemComposer extends MessageComposer {
    private RoomUnit from;
    private int handItem;

    public RoomUserReceivedHandItemComposer(RoomUnit from, int handItem) {
        this.from = from;
        this.handItem = handItem;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomUserReceivedHandItemComposer);
        this.response.appendInt(this.from.getId());
        this.response.appendInt(this.handItem);
        return this.response;
    }

    public RoomUnit getFrom() {
        return from;
    }

    public int getHandItem() {
        return handItem;
    }
}
