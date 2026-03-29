package com.eu.arcturus.habbohotel.catalog.layouts;

import com.eu.arcturus.habbohotel.catalog.CatalogPage;
import com.eu.arcturus.messages.ServerMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketplaceLayout extends CatalogPage {
    public MarketplaceLayout(ResultSet set) throws SQLException {
        super(set);
    }

    @Override
    public void serialize(ServerMessage message) {
        message.appendString("marketplace");
        message.appendInt(0);
        message.appendInt(0);
    }
}
