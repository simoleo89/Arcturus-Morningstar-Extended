package com.eu.arcturus.habbohotel.items.interactions.games.battlebanzai.scoreboards;

import com.eu.arcturus.habbohotel.games.GameTeamColors;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.items.interactions.games.InteractionGameScoreboard;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractionBattleBanzaiScoreboard extends InteractionGameScoreboard {
    public InteractionBattleBanzaiScoreboard(ResultSet set, Item baseItem, GameTeamColors teamColor) throws SQLException {
        super(set, baseItem, teamColor);
    }

    public InteractionBattleBanzaiScoreboard(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells, GameTeamColors teamColor) {
        super(id, userId, item, extradata, limitedStack, limitedSells, teamColor);
    }

    @Override
    public boolean canWalkOn(RoomUnit roomUnit, Room room, Object[] objects) {
        return false;
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public void onWalk(RoomUnit roomUnit, Room room, Object[] objects) throws Exception {

    }
}
