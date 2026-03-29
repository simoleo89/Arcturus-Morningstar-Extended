package com.eu.arcturus.messages.outgoing.wired;

import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredEffect;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class WiredEffectDataComposer extends MessageComposer {
    private final InteractionWiredEffect effect;
    private final Room room;

    public WiredEffectDataComposer(InteractionWiredEffect effect, Room room) {
        this.effect = effect;
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.WiredEffectDataComposer);
        this.effect.serializeWiredData(this.response, this.room);
        this.effect.needsUpdate(true);
        return this.response;
    }

    public InteractionWiredEffect getEffect() {
        return effect;
    }

    public Room getRoom() {
        return room;
    }
}
