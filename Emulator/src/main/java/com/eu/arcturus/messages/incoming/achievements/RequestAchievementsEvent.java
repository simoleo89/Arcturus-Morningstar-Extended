package com.eu.arcturus.messages.incoming.achievements;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.achievements.AchievementListComposer;

public class RequestAchievementsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new AchievementListComposer(this.client.getHabbo()));
    }
}
