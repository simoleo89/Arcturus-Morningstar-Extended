package com.eu.arcturus.messages.outgoing.camera;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class CameraCompetitionStatusComposer extends MessageComposer {
    private final boolean unknownBoolean;
    private final String unknownString;

    public CameraCompetitionStatusComposer(boolean unknownBoolean, String unknownString) {
        this.unknownBoolean = unknownBoolean;
        this.unknownString = unknownString;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.CameraCompetitionStatusComposer);
        this.response.appendBoolean(this.unknownBoolean);
        this.response.appendString(this.unknownString);
        return this.response;
    }

    public boolean isUnknownBoolean() {
        return unknownBoolean;
    }

    public String getUnknownString() {
        return unknownString;
    }
}