package com.eu.arcturus.plugin.events.users.catalog;

import com.eu.arcturus.habbohotel.catalog.CatalogItem;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import java.util.HashSet;

public class UserCatalogFurnitureBoughtEvent extends UserCatalogEvent {

    public final HashSet<HabboItem> furniture;


    public UserCatalogFurnitureBoughtEvent(Habbo habbo, CatalogItem catalogItem, HashSet<HabboItem> furniture) {
        super(habbo, catalogItem);

        this.furniture = furniture;
    }
}
