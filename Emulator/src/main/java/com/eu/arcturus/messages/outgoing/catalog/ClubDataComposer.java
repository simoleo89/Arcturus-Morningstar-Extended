package com.eu.arcturus.messages.outgoing.catalog;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.catalog.ClubOffer;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.List;

public class ClubDataComposer extends MessageComposer {
    private final int windowId;
    private final Habbo habbo;

    public ClubDataComposer(Habbo habbo, int windowId) {
        this.habbo = habbo;
        this.windowId = windowId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.ClubDataComposer);

        List<ClubOffer> offers = Emulator.getGameEnvironment().getCatalogManager().getClubOffers();
        this.response.appendInt(offers.size());

        //TODO Change this to a seperate table.
        for (ClubOffer offer : offers) {
            offer.serialize(this.response, this.habbo.getHabboStats().getClubExpireTimestamp());
        }

        this.response.appendInt(this.windowId);
        return this.response;
    }

    public int getWindowId() {
        return windowId;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
