package com.eu.arcturus.habbohotel.items.interactions;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomMoodlightData;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractionMoodLight extends HabboItem {
    public InteractionMoodLight(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public InteractionMoodLight(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public void serializeExtradata(ServerMessage serverMessage) {
        serverMessage.appendInt((this.isLimited() ? 256 : 0));
        serverMessage.appendString(this.getExtradata());

        super.serializeExtradata(serverMessage);
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

    @Override
    public void onPlace(Room room) {
        if (room != null) {
            for (RoomMoodlightData data : room.getMoodlightData().values()) {
                if (data.isEnabled()) {
                    this.setExtradata(data.toString());
                    this.needsUpdate(true);
                    room.updateItem(this);
                    Emulator.getThreading().run(this);
                }
            }
        }

        super.onPlace(room);
    }

    @Override
    public boolean isUsable() {
        return true;
    }
}
