package com.eu.arcturus.plugin.events.users.catalog;

import com.eu.arcturus.habbohotel.catalog.CatalogItem;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import java.util.HashSet;

import java.util.List;

public class UserCatalogItemPurchasedEvent extends UserCatalogEvent {

    public final HashSet<HabboItem> itemsList;


    public int totalCredits;


    public int totalPoints;


    public List<String> badges;


    public UserCatalogItemPurchasedEvent(Habbo habbo, CatalogItem catalogItem, HashSet<HabboItem> itemsList, int totalCredits, int totalPoints, List<String> badges) {
        super(habbo, catalogItem);

        this.itemsList = itemsList;
        this.totalCredits = totalCredits;
        this.totalPoints = totalPoints;
        this.badges = badges;
    }
}