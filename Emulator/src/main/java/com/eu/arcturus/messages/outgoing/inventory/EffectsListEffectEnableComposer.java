package com.eu.arcturus.messages.outgoing.inventory;

import com.eu.arcturus.habbohotel.users.inventory.EffectsComponent;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class EffectsListEffectEnableComposer extends MessageComposer {
    public final EffectsComponent.HabboEffect effect;

    public EffectsListEffectEnableComposer(EffectsComponent.HabboEffect effect) {
        this.effect = effect;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.EffectsListEffectEnableComposer);
        this.response.appendInt(this.effect.effect); //Type
        this.response.appendInt(this.effect.duration); //Duration
        this.response.appendBoolean(this.effect.enabled); //activated
        return this.response;
    }

    public EffectsComponent.HabboEffect getEffect() {
        return effect;
    }
}