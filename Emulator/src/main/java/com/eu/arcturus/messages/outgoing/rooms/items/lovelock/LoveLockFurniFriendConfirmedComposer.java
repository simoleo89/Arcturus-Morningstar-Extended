package com.eu.arcturus.messages.outgoing.rooms.items.lovelock;

import com.eu.arcturus.habbohotel.items.interactions.InteractionLoveLock;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class LoveLockFurniFriendConfirmedComposer extends MessageComposer {
    private final InteractionLoveLock loveLock;

    public LoveLockFurniFriendConfirmedComposer(InteractionLoveLock loveLock) {
        this.loveLock = loveLock;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.LoveLockFurniFriendConfirmedComposer);
        this.response.appendInt(this.loveLock.getId());
        return this.response;
    }

    public InteractionLoveLock getLoveLock() {
        return loveLock;
    }
}
