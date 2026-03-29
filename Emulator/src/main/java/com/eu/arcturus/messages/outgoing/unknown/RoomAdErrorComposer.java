package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomAdErrorComposer extends MessageComposer {
    private final int errorCode;
    private final String unknownString;

    public RoomAdErrorComposer(int errorCode, String unknownString) {
        this.errorCode = errorCode;
        this.unknownString = unknownString;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomAdErrorComposer);
        this.response.appendInt(this.errorCode);
        this.response.appendString(this.unknownString);
        return this.response;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getUnknownString() {
        return unknownString;
    }
}