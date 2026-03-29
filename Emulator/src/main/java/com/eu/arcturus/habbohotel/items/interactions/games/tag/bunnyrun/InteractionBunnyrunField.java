package com.eu.arcturus.habbohotel.items.interactions.games.tag.bunnyrun;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.achievements.AchievementManager;
import com.eu.arcturus.habbohotel.games.tag.BunnyrunGame;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.items.interactions.games.tag.InteractionTagField;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractionBunnyrunField extends InteractionTagField {
    public InteractionBunnyrunField(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem, BunnyrunGame.class);
    }

    public InteractionBunnyrunField(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells, BunnyrunGame.class);
    }

    @Override
    public void onPlace(Room room) {
        super.onPlace(room);

        Habbo itemOwner = Emulator.getGameEnvironment().getHabboManager().getHabbo(this.getUserId());

        if (itemOwner != null) {
            AchievementManager.progressAchievement(itemOwner, Emulator.getGameEnvironment().getAchievementManager().getAchievement("RbBunnyTag"));
        }
    }
}