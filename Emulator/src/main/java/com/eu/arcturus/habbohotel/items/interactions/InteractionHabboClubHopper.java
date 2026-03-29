package com.eu.arcturus.habbohotel.items.interactions;

import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.messages.outgoing.generic.alerts.CustomNotificationComposer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractionHabboClubHopper extends InteractionHopper {
    public InteractionHabboClubHopper(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public InteractionHabboClubHopper(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public void onClick(GameClient client, Room room, Object[] objects) throws Exception {
        if (client.getHabbo().getHabboStats().hasActiveClub()) {
            super.onClick(client, room, objects);
        } else {
            client.sendResponse(new CustomNotificationComposer(CustomNotificationComposer.HOPPER_NO_HC));
        }
    }

    @Override
    protected boolean canUseTeleport(GameClient client, RoomTile front, Room room) {
        return super.canUseTeleport(client, front, room) && client.getHabbo().getHabboStats().hasActiveClub();
    }
}
