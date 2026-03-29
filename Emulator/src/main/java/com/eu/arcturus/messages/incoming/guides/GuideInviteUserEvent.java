package com.eu.arcturus.messages.incoming.guides;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guides.GuideTour;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.guides.GuideSessionInvitedToGuideRoomComposer;

public class GuideInviteUserEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        GuideTour tour = Emulator.getGameEnvironment().getGuideManager().getGuideTourByHelper(this.client.getHabbo());

        if (tour != null) {
            ServerMessage message = new GuideSessionInvitedToGuideRoomComposer(this.client.getHabbo().getHabboInfo().getCurrentRoom()).compose();
            tour.getNoob().getClient().sendResponse(message);
            tour.getHelper().getClient().sendResponse(message);
        }
    }
}
