package com.eu.arcturus.messages.incoming.events.calendar;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class AdventCalendarOpenDayEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String campaignName = this.packet.readString();
        int day = this.packet.readInt();

        Emulator.getGameEnvironment().getCalendarManager().claimCalendarReward(this.client.getHabbo(), campaignName, day, false);
    }
}