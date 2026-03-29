package com.eu.arcturus.habbohotel.items.interactions;

import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.outgoing.generic.alerts.CustomNotificationComposer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractionCostumeHopper extends InteractionHopper {
    public InteractionCostumeHopper(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public InteractionCostumeHopper(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public void onClick(GameClient client, Room room, Object[] objects) throws Exception {
        if (client.getHabbo().getRoomUnit().getEffectId() > 0) {
            super.onClick(client, room, objects);
        } else {
            client.sendResponse(new CustomNotificationComposer(CustomNotificationComposer.HOPPER_NO_COSTUME));
        }
    }
}
