package com.eu.arcturus.messages.outgoing.inventory;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.inventory.EffectsComponent;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.Collection;


public class UserEffectsListComposer extends MessageComposer {
    public final Habbo habbo;
    public final Collection<EffectsComponent.HabboEffect> effects;

    public UserEffectsListComposer(Habbo habbo, Collection<EffectsComponent.HabboEffect> effects) {
        this.habbo = habbo;
        this.effects = effects;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UserEffectsListComposer);


        if (this.habbo == null || this.habbo.getInventory() == null || this.habbo.getInventory().getEffectsComponent() == null || this.habbo.getInventory().getEffectsComponent().effects == null) {
            this.response.appendInt(0);
        } else {
            synchronized (this.habbo.getInventory().getEffectsComponent().effects) {
                this.response.appendInt(this.effects.size());

                for (EffectsComponent.HabboEffect effect : effects) {
                    UserEffectsListComposer.this.response.appendInt(effect.effect);
                    UserEffectsListComposer.this.response.appendInt(0);
                    UserEffectsListComposer.this.response.appendInt(effect.duration > 0 ? effect.duration : Integer.MAX_VALUE);
                    UserEffectsListComposer.this.response.appendInt((effect.duration > 0 ? (effect.total - (effect.isActivated() ? 1 : 0)) : 0));

                    if(!effect.isActivated() && effect.duration > 0) {
                        UserEffectsListComposer.this.response.appendInt(0);
                    }
                    else {
                        UserEffectsListComposer.this.response.appendInt(effect.duration > 0 ? (Emulator.getIntUnixTimestamp() - effect.activationTimestamp) + effect.duration : 0);
                    }
                    UserEffectsListComposer.this.response.appendBoolean(effect.duration <= 0); // is perm
                }
            }
        }
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }

    public Collection<EffectsComponent.HabboEffect> getEffects() {
        return effects;
    }
}
