package com.eu.arcturus.plugin.events.users.achievements;

import com.eu.arcturus.habbohotel.achievements.Achievement;
import com.eu.arcturus.habbohotel.users.Habbo;

public class UserAchievementProgressEvent extends UserAchievementEvent {

    public final int progressed;


    public UserAchievementProgressEvent(Habbo habbo, Achievement achievement, int progressed) {
        super(habbo, achievement);

        this.progressed = progressed;
    }
}
