package com.eu.arcturus.messages.incoming.catalog.catalogadmin;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.CatalogUpdatedComposer;
import com.eu.arcturus.messages.outgoing.catalog.catalogadmin.CatalogAdminResultComposer;

public class CatalogAdminPublishEvent extends MessageHandler {

    @Override
    public void handle() throws Exception {
        if (!this.client.getHabbo().hasPermission(Permission.ACC_CATALOGFURNI)) {
            this.client.sendResponse(new CatalogAdminResultComposer(false, "No permission"));
            return;
        }

        // Reload the entire catalog from database
        Emulator.getGameEnvironment().getCatalogManager().initialize();

        // Notify all connected clients that the catalog has been updated
        Emulator.getGameServer().getGameClientManager().sendBroadcastResponse(new CatalogUpdatedComposer());

        this.client.sendResponse(new CatalogAdminResultComposer(true, "Catalog published"));
    }
}
