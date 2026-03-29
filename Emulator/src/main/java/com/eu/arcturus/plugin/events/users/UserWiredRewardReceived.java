package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.items.interactions.wired.effects.WiredEffectGiveReward;
import com.eu.arcturus.habbohotel.users.Habbo;

public class UserWiredRewardReceived extends UserEvent {

    public final WiredEffectGiveReward wiredEffectGiveReward;


    public final String type;


    public String value;


    public UserWiredRewardReceived(Habbo habbo, WiredEffectGiveReward wiredEffectGiveReward, String type, String value) {
        super(habbo);

        this.wiredEffectGiveReward = wiredEffectGiveReward;
        this.type = type;
        this.value = value;
    }
}
