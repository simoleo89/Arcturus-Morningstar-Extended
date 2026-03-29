package com.eu.arcturus.messages.rcon;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.achievements.Achievement;
import com.eu.arcturus.habbohotel.achievements.AchievementManager;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.google.gson.Gson;

public class ProgressAchievement extends RCONMessage<ProgressAchievement.ProgressAchievementJSON> {

    public ProgressAchievement() {
        super(ProgressAchievementJSON.class);
    }

    @Override
    public void handle(Gson gson, ProgressAchievementJSON json) {
        Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(json.user_id);

        if (habbo != null) {
            Achievement achievement = Emulator.getGameEnvironment().getAchievementManager().getAchievement(json.achievement_id);
            if (achievement != null) {
                AchievementManager.progressAchievement(habbo, achievement, json.progress);
            } else {
                this.status = RCONMessage.STATUS_ERROR;
            }
        } else {
            this.status = RCONMessage.HABBO_NOT_FOUND;
        }
    }

    static class ProgressAchievementJSON {

        public int user_id;


        public int achievement_id;


        public int progress;
    }
}