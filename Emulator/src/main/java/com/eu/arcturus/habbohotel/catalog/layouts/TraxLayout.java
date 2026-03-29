package com.eu.arcturus.habbohotel.catalog.layouts;

import com.eu.arcturus.habbohotel.catalog.CatalogPage;
import com.eu.arcturus.messages.ServerMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TraxLayout extends CatalogPage {
    public TraxLayout(ResultSet set) throws SQLException {
        super(set);
    }

    @Override
    public void serialize(ServerMessage message) {
        message.appendString("soundmachine");
        message.appendInt(2);
        message.appendString(super.getHeaderImage());
        message.appendString(super.getTeaserImage());
        message.appendInt(2);
        message.appendString(super.getTextOne());
        message.appendString(super.getTextDetails());
    }
}
