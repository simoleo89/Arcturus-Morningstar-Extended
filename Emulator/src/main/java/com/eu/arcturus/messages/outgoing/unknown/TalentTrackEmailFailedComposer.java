package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class TalentTrackEmailFailedComposer extends MessageComposer {
    private final int result;

    public TalentTrackEmailFailedComposer(int result) {
        this.result = result;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.TalentTrackEmailFailedComposer);
        this.response.appendInt(this.result);
        return this.response;
    }

    public int getResult() {
        return result;
    }
}