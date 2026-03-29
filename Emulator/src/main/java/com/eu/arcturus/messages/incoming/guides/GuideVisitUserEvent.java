package com.eu.arcturus.messages.incoming.guides;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guides.GuideTour;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.guides.GuideSessionRequesterRoomComposer;

public class GuideVisitUserEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        GuideTour tour = Emulator.getGameEnvironment().getGuideManager().getGuideTourByHelper(this.client.getHabbo());

        if (tour != null) {
            this.client.sendResponse(new GuideSessionRequesterRoomComposer(tour.getNoob().getHabboInfo().getCurrentRoom()));
        }
    }
}
