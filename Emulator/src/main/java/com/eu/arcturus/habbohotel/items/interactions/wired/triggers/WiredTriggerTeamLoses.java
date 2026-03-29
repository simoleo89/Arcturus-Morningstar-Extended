package com.eu.arcturus.habbohotel.items.interactions.wired.triggers;

import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.wired.WiredTriggerType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WiredTriggerTeamLoses extends WiredTriggerGameStarts {
    public WiredTriggerTeamLoses(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public WiredTriggerTeamLoses(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public WiredTriggerType getType() {
        return WiredTriggerType.CUSTOM;
    }
}
