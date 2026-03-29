package com.eu.arcturus.messages.outgoing.rooms.items.lovelock;

import com.eu.arcturus.habbohotel.items.interactions.InteractionLoveLock;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class LoveLockFurniStartComposer extends MessageComposer {
    private final InteractionLoveLock loveLock;

    public LoveLockFurniStartComposer(InteractionLoveLock loveLock) {
        this.loveLock = loveLock;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.LoveLockFurniStartComposer);
        this.response.appendInt(this.loveLock.getId());
        this.response.appendBoolean(true);
        return this.response;
    }

    public InteractionLoveLock getLoveLock() {
        return loveLock;
    }
}
