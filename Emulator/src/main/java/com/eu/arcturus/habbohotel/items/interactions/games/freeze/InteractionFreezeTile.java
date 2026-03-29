package com.eu.arcturus.habbohotel.items.interactions.games.freeze;

import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.games.freeze.FreezeGame;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import java.util.HashSet;
import org.apache.commons.math3.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InteractionFreezeTile extends HabboItem {
    public InteractionFreezeTile(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
        this.setExtradata("0");
    }

    public InteractionFreezeTile(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
        this.setExtradata("0");
    }

    @Override
    public boolean canWalkOn(RoomUnit roomUnit, Room room, Object[] objects) {
        return true;
    }

    @Override
    public boolean isWalkable() {
        return true;
    }

    @Override
    public void onClick(GameClient client, Room room, Object[] objects) throws Exception {
        if (client == null)
            return;

        if (client.getHabbo().getRoomUnit().getCurrentLocation().x == this.getX() && client.getHabbo().getRoomUnit().getCurrentLocation().y == this.getY()) {
            FreezeGame game = (FreezeGame) room.getGame(FreezeGame.class);

            if (game != null)
                game.throwBall(client.getHabbo(), this);
        }
    }

    @Override
    public void onWalk(RoomUnit roomUnit, Room room, Object[] objects) throws Exception {

    }

    @Override
    public void serializeExtradata(ServerMessage serverMessage) {
        serverMessage.appendInt((this.isLimited() ? 256 : 0));
        serverMessage.appendString(this.getExtradata());

        super.serializeExtradata(serverMessage);
    }

    @Override
    public void onPickUp(Room room) {
        this.setExtradata("0");
    }

    @Override
    public boolean allowWiredResetState() {
        return false;
    }


    @Override
    public boolean canStackAt(Room room, List<Pair<RoomTile, HashSet<HabboItem>>> itemsAtLocation) {
        for (Pair<RoomTile, HashSet<HabboItem>> set : itemsAtLocation) {
            if (set.getValue() != null && !set.getValue().isEmpty()) return false;
        }

        return super.canStackAt(room, itemsAtLocation);
    }
}
