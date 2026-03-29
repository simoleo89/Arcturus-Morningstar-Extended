package com.eu.arcturus.messages.outgoing.guides;

import com.eu.arcturus.habbohotel.guides.GuideTour;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuideSessionStartedComposer extends MessageComposer {
    private final GuideTour tour;

    public GuideSessionStartedComposer(GuideTour tour) {
        this.tour = tour;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuideSessionStartedComposer);
        this.response.appendInt(this.tour.getNoob().getHabboInfo().getId());
        this.response.appendString(this.tour.getNoob().getHabboInfo().getUsername());
        this.response.appendString(this.tour.getNoob().getHabboInfo().getLook());
        this.response.appendInt(this.tour.getHelper().getHabboInfo().getId());
        this.response.appendString(this.tour.getHelper().getHabboInfo().getUsername());
        this.response.appendString(this.tour.getHelper().getHabboInfo().getLook());
        return this.response;
    }

    public GuideTour getTour() {
        return tour;
    }
}
