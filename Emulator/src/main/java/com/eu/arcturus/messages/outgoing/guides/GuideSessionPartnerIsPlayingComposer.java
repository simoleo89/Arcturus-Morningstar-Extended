package com.eu.arcturus.messages.outgoing.guides;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuideSessionPartnerIsPlayingComposer extends MessageComposer {
    public final boolean isPlaying;

    public GuideSessionPartnerIsPlayingComposer(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuideSessionPartnerIsPlayingComposer);
        this.response.appendBoolean(this.isPlaying);
        return this.response;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}