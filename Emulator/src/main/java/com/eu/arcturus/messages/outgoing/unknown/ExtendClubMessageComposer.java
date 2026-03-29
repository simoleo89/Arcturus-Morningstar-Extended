package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.habbohotel.catalog.ClubOffer;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class ExtendClubMessageComposer extends MessageComposer {
    private final Habbo habbo;
    private final ClubOffer offer;
    private final int normalCreditCost;
    private final int normalPointsCost;
    private final int pointsType;
    private final int daysRemaining;

    public ExtendClubMessageComposer(Habbo habbo, ClubOffer offer, int normalCreditCost, int normalPointsCost, int pointsType, int daysRemaining) {
        this.habbo = habbo;
        this.offer = offer;
        this.normalCreditCost = normalCreditCost;
        this.normalPointsCost = normalPointsCost;
        this.pointsType = pointsType;
        this.daysRemaining = daysRemaining;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.ExtendClubMessageComposer);
        this.offer.serialize(this.response, this.habbo.getHabboStats().getClubExpireTimestamp());

        this.response.appendInt(this.normalCreditCost);
        this.response.appendInt(this.normalPointsCost);
        this.response.appendInt(this.pointsType);
        this.response.appendInt(this.daysRemaining);
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }

    public ClubOffer getOffer() {
        return offer;
    }

    public int getNormalCreditCost() {
        return normalCreditCost;
    }

    public int getNormalPointsCost() {
        return normalPointsCost;
    }

    public int getPointsType() {
        return pointsType;
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }
}