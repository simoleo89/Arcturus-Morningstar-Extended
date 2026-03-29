package com.eu.arcturus.messages.incoming.achievements;

import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.inventory.InventoryAchievementsComposer;

public class RequestAchievementConfigurationEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new InventoryAchievementsComposer());
    }
}
