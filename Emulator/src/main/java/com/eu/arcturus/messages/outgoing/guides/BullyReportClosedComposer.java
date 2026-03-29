package com.eu.arcturus.messages.outgoing.guides;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class BullyReportClosedComposer extends MessageComposer {
    public final static int CLOSED = 1;
    public final static int MISUSE = 2;

    public final int code;

    public BullyReportClosedComposer(int code) {
        this.code = code;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.BullyReportClosedComposer);
        this.response.appendInt(this.code);
        return this.response;
    }

    public int getCode() {
        return code;
    }
}
