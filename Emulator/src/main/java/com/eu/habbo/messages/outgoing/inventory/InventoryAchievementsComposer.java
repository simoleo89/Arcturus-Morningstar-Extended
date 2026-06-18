package com.eu.habbo.messages.outgoing.inventory;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.achievements.Achievement;
import com.eu.habbo.habbohotel.achievements.AchievementLevel;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;
import java.util.HashMap;

public class InventoryAchievementsComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.InventoryAchievementsComposer);

        synchronized (Emulator.getGameEnvironment().getAchievementManager().getAchievements()) {
            HashMap<String, Achievement> achievements = Emulator.getGameEnvironment().getAchievementManager().getAchievements();

            this.response.appendInt(achievements.size());
            for (Achievement achievement : achievements.values()) {
                this.response.appendString((achievement.name.startsWith("ACH_") ? achievement.name.replace("ACH_", "") : achievement.name));
                this.response.appendInt(achievement.levels.size());

                for (AchievementLevel level : achievement.levels.values()) {
                    this.response.appendInt(level.level);
                    this.response.appendInt(level.progress);
                }
            }
        }
        return this.response;
    }
}
