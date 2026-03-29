package com.eu.arcturus.messages.outgoing.catalog.catalogadmin;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class CatalogAdminResultComposer extends MessageComposer {
    private final boolean success;
    private final String message;

    public CatalogAdminResultComposer(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.CatalogAdminResultComposer);
        this.response.appendBoolean(this.success);
        this.response.appendString(this.message);
        return this.response;
    }
}
