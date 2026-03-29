package com.eu.arcturus.messages.incoming.gamecenter;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.gamecenter.GameCenterAchievementsConfigurationComposer;

public class GameCenterRequestGamesEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new GameCenterAchievementsConfigurationComposer());
    }
}