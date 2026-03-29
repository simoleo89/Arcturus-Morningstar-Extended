package com.eu.arcturus.plugin.events.users.achievements;

import com.eu.arcturus.habbohotel.achievements.Achievement;
import com.eu.arcturus.habbohotel.achievements.AchievementLevel;
import com.eu.arcturus.habbohotel.users.Habbo;

public class UserAchievementLeveledEvent extends UserAchievementEvent {

    public final AchievementLevel oldLevel;


    public final AchievementLevel newLevel;


    public UserAchievementLeveledEvent(Habbo habbo, Achievement achievement, AchievementLevel oldLevel, AchievementLevel newLevel) {
        super(habbo, achievement);

        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }
}
