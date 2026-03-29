package com.eu.arcturus.habbohotel.items.interactions;

import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.items.interactions.wired.WiredSettings;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.messages.incoming.wired.WiredSaveException;
import com.eu.arcturus.messages.outgoing.wired.WiredExtraDataComposer;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class InteractionWiredExtra extends InteractionWired {
    protected InteractionWiredExtra(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    protected InteractionWiredExtra(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public void onClick(GameClient client, Room room, Object[] objects) throws Exception {
        if (client != null) {
            if (room.hasRights(client.getHabbo())) {
                if (this.hasConfiguration()) {
                    client.sendResponse(new WiredExtraDataComposer(this, room));
                }
                this.activateBox(room);
            }
        }
    }

    @Override
    public boolean canWalkOn(RoomUnit roomUnit, Room room, Object[] objects) {
        return true;
    }

    @Override
    public boolean isWalkable() {
        return true;
    }

    public boolean saveData(WiredSettings settings, GameClient gameClient) throws WiredSaveException {
        return true;
    }

    public boolean hasConfiguration() {
        return false;
    }
}
