package com.eu.arcturus.messages.outgoing.hotelview;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class HotelViewHideCommunityVoteButtonComposer extends MessageComposer {
    private final boolean unknownBoolean;

    public HotelViewHideCommunityVoteButtonComposer(boolean unknownBoolean) {
        this.unknownBoolean = unknownBoolean;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.HotelViewHideCommunityVoteButtonComposer);
        this.response.appendBoolean(this.unknownBoolean);
        return this.response;
    }

    public boolean isUnknownBoolean() {
        return unknownBoolean;
    }
}