package com.eu.arcturus.messages.incoming.catalog.catalogadmin;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.catalog.CatalogPage;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.catalogadmin.CatalogAdminResultComposer;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CatalogAdminDeletePageEvent extends MessageHandler {

    @Override
    public void handle() throws Exception {
        if (!this.client.getHabbo().hasPermission(Permission.ACC_CATALOGFURNI)) {
            this.client.sendResponse(new CatalogAdminResultComposer(false, "No permission"));
            return;
        }

        int pageId = this.packet.readInt();

        CatalogPage page = Emulator.getGameEnvironment().getCatalogManager().catalogPages.get(pageId);

        if (page == null) {
            this.client.sendResponse(new CatalogAdminResultComposer(false, "Page not found: " + pageId));
            return;
        }

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM catalog_pages WHERE id = ?")) {
            statement.setInt(1, pageId);
            statement.execute();
        }

        Emulator.getGameEnvironment().getCatalogManager().catalogPages.remove(pageId);

        this.client.sendResponse(new CatalogAdminResultComposer(true, "Page deleted"));
    }
}
