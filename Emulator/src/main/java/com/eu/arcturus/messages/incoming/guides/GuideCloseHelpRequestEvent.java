package com.eu.arcturus.messages.incoming.guides;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guides.GuideTour;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class GuideCloseHelpRequestEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        GuideTour tour = Emulator.getGameEnvironment().getGuideManager().getGuideTourByHabbo(this.client.getHabbo());

        if (tour != null) {
            Emulator.getGameEnvironment().getGuideManager().endSession(tour);
        }
    }
}
