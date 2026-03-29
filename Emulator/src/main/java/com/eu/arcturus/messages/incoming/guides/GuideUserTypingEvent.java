package com.eu.arcturus.messages.incoming.guides;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guides.GuideTour;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.guides.GuideSessionPartnerIsTypingComposer;

public class GuideUserTypingEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        boolean typing = this.packet.readBoolean();

        GuideTour tour = Emulator.getGameEnvironment().getGuideManager().getGuideTourByHabbo(this.client.getHabbo());

        if (tour != null) {
            if (tour.getHelper() == this.client.getHabbo()) {
                tour.getNoob().getClient().sendResponse(new GuideSessionPartnerIsTypingComposer(typing));
            } else {
                tour.getHelper().getClient().sendResponse(new GuideSessionPartnerIsTypingComposer(typing));
            }
        }
    }
}
