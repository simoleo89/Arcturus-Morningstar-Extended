package com.eu.arcturus.messages.outgoing.wired;

import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredExtra;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class WiredExtraDataComposer extends MessageComposer {
    private final InteractionWiredExtra extra;
    private final Room room;

    public WiredExtraDataComposer(InteractionWiredExtra extra, Room room) {
        this.extra = extra;
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.WiredEffectDataComposer);
        this.extra.serializeWiredData(this.response, this.room);
        this.extra.needsUpdate(true);
        return this.response;
    }
}
