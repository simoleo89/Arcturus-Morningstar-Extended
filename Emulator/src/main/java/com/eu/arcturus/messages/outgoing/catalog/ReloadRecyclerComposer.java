package com.eu.arcturus.messages.outgoing.catalog;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class ReloadRecyclerComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.ReloadRecyclerComposer);
        this.response.appendInt(1);
        this.response.appendInt(0);
        return this.response;
    }
}
