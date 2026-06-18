package com.eu.habbo.messages.incoming.catalog;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.catalog.CatalogItem;
import com.eu.habbo.habbohotel.catalog.CatalogPage;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.catalog.CatalogSearchResultComposer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class CatalogSearchedItemEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int offerId = this.packet.readInt();

        int pageId = Emulator.getGameEnvironment().getCatalogManager().offerDefs.get(offerId);

        if (pageId != 0) {
            CatalogPage page = Emulator.getGameEnvironment().getCatalogManager().getCatalogPage(Emulator.getGameEnvironment().getCatalogManager().getCatalogItem(pageId).getPageId());

            if (page != null) {
                for (Int2ObjectMap.Entry<CatalogItem> entry : page.getCatalogItems().int2ObjectEntrySet()) {
                    CatalogItem item = entry.getValue();

                    if (item.getOfferId() == offerId) {
                        this.client.sendResponse(new CatalogSearchResultComposer(item));
                        return;
                    }
                }
            }
        }
    }
}
