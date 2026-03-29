package com.eu.arcturus.plugin.events.users.catalog;

import com.eu.arcturus.habbohotel.catalog.CatalogItem;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.events.users.UserEvent;

public class UserCatalogEvent extends UserEvent {

    public CatalogItem catalogItem;


    public UserCatalogEvent(Habbo habbo, CatalogItem catalogItem) {
        super(habbo);

        this.catalogItem = catalogItem;
    }
}