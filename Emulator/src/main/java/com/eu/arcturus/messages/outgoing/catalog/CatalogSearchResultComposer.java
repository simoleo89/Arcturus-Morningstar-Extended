package com.eu.arcturus.messages.outgoing.catalog;

import com.eu.arcturus.habbohotel.catalog.CatalogItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class CatalogSearchResultComposer extends MessageComposer {
    private final CatalogItem item;

    public CatalogSearchResultComposer(CatalogItem item) {
        this.item = item;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.CatalogSearchResultComposer);
        this.item.serialize(this.response);
        return this.response;
    }

    public CatalogItem getItem() {
        return item;
    }
}
