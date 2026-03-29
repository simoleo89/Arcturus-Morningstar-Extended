package com.eu.arcturus.messages.incoming.catalog;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.catalog.CatalogItem;
import com.eu.arcturus.habbohotel.catalog.CatalogPage;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.CatalogSearchResultComposer;


public class CatalogSearchedItemEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int offerId = this.packet.readInt();

        int pageId = Emulator.getGameEnvironment().getCatalogManager().offerDefs.get(offerId);

        if (pageId != 0) {
            CatalogPage page = Emulator.getGameEnvironment().getCatalogManager().getCatalogPage(Emulator.getGameEnvironment().getCatalogManager().getCatalogItem(pageId).getPageId());

            if (page != null) {
                for (CatalogItem item : page.getCatalogItems().values()) {
                    if (item.getOfferId() == offerId) {
                        this.client.sendResponse(new CatalogSearchResultComposer(item));
                        return;
                    }
                }
            }
        }
    }
}
