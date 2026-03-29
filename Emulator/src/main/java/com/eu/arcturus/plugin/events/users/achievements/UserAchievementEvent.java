package com.eu.arcturus.plugin.events.users.achievements;

import com.eu.arcturus.habbohotel.achievements.Achievement;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.events.users.UserEvent;

public abstract class UserAchievementEvent extends UserEvent {

    public final Achievement achievement;


    public UserAchievementEvent(Habbo habbo, Achievement achievement) {
        super(habbo);

        this.achievement = achievement;
    }
}
