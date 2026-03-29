package com.eu.arcturus.habbohotel.items.interactions.games;

import com.eu.arcturus.habbohotel.games.GameTeamColors;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.users.HabboItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class InteractionGameTeamItem extends HabboItem {
    public final GameTeamColors teamColor;

    protected InteractionGameTeamItem(ResultSet set, Item baseItem, GameTeamColors teamColor) throws SQLException {
        super(set, baseItem);

        this.teamColor = teamColor;
    }

    protected InteractionGameTeamItem(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells, GameTeamColors teamColor) {
        super(id, userId, item, extradata, limitedStack, limitedSells);

        this.teamColor = teamColor;
    }
}
