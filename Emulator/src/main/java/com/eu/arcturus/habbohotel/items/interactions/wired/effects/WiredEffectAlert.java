package com.eu.arcturus.habbohotel.items.interactions.wired.effects;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.wired.core.WiredContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WiredEffectAlert extends WiredEffectWhisper {
    public WiredEffectAlert(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public WiredEffectAlert(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public void execute(WiredContext ctx) {
        Room room = ctx.room();

        for (com.eu.arcturus.habbohotel.rooms.RoomUnit unit : resolveUsers(ctx)) {
            Habbo habbo = room.getHabbo(unit);
            if (habbo == null) continue;

            habbo.alert(this.message
                    .replace("%online%", Emulator.getGameEnvironment().getHabboManager().getOnlineCount() + "")
                    .replace("%username%", habbo.getHabboInfo().getUsername())
                    .replace("%roomsloaded%", Emulator.getGameEnvironment().getRoomManager().loadedRoomsCount() + ""));
        }
    }
}
