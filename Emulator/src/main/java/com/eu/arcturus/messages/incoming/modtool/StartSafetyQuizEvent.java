package com.eu.arcturus.messages.incoming.modtool;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.achievements.AchievementManager;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class StartSafetyQuizEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.packet.readString();

        AchievementManager.progressAchievement(this.client.getHabbo(), Emulator.getGameEnvironment().getAchievementManager().getAchievement("SafetyQuizGraduate"));
    }
}