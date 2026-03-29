package com.eu.arcturus.messages.outgoing.inventory;

import com.eu.arcturus.habbohotel.users.inventory.EffectsComponent;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class EffectsListRemoveComposer extends MessageComposer {
    public final EffectsComponent.HabboEffect effect;

    public EffectsListRemoveComposer(EffectsComponent.HabboEffect effect) {
        this.effect = effect;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.EffectsListRemoveComposer);
        this.response.appendInt(this.effect.effect);
        return this.response;
    }

    public EffectsComponent.HabboEffect getEffect() {
        return effect;
    }
}