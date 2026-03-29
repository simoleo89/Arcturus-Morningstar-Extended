package com.eu.arcturus.messages.outgoing.guides;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuideSessionEndedComposer extends MessageComposer {
    public static final int SOMETHING_WRONG = 0;
    public static final int HELP_CASE_CLOSED = 1;

    private final int errorCode;

    public GuideSessionEndedComposer(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuideSessionEndedComposer);
        this.response.appendInt(this.errorCode); //?
        return this.response;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
