package com.eu.arcturus.messages.outgoing.catalog;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class CatalogModeComposer extends MessageComposer {
    private final int mode;

    public CatalogModeComposer(int mode) {
        this.mode = mode;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.CatalogModeComposer);
        this.response.appendInt(this.mode);
        return this.response;
    }

    public int getMode() {
        return mode;
    }
}
