package com.eu.arcturus.habbohotel.items.interactions;

import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import java.util.HashMap;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractionRoomAds extends InteractionCustomValues {
    public final static HashMap<String, String> defaultValues = new HashMap<String, String>() {
        {
            this.put("imageUrl", "");
        }

        {
            this.put("clickUrl", "");
        }

        {
            this.put("offsetX", "0");
        }

        {
            this.put("offsetY", "0");
        }

        {
            this.put("offsetZ", "0");
        }
    };

    public InteractionRoomAds(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem, defaultValues);
    }

    public InteractionRoomAds(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells, defaultValues);
    }

    @Override
    public boolean canWalkOn(RoomUnit roomUnit, Room room, Object[] objects) {
        return true;
    }

    @Override
    public boolean isWalkable() {
        return true;
    }
}
