package com.eu.arcturus.messages.outgoing.gamecenter.basejump;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class BaseJumpUnloadGameComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.BaseJumpUnloadGameComposer);
        this.response.appendInt(3);
        this.response.appendString("basejump");
        return this.response;
    }
}