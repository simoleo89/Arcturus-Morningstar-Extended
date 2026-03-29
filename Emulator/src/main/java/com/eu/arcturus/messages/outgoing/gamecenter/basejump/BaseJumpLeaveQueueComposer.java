package com.eu.arcturus.messages.outgoing.gamecenter.basejump;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class BaseJumpLeaveQueueComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.BaseJumpLeaveQueueComposer);
        this.response.appendInt(3);
        return this.response;
    }
}