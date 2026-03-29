package com.eu.arcturus.habbohotel.items.interactions.games;

import com.eu.arcturus.habbohotel.games.Game;
import com.eu.arcturus.habbohotel.games.GameTeamColors;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.messages.ServerMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class InteractionGameGate extends InteractionGameTeamItem {
    public InteractionGameGate(ResultSet set, Item baseItem, GameTeamColors teamColor) throws SQLException {
        super(set, baseItem, teamColor);
        this.setExtradata("0");
    }

    public InteractionGameGate(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells, GameTeamColors teamColor) {
        super(id, userId, item, extradata, limitedStack, limitedSells, teamColor);
        this.setExtradata("0");
    }

    @Override
    public void onPickUp(Room room) {
        this.setExtradata("0");
    }

    @Override
    public void onWalkOn(RoomUnit roomUnit, Room room, Object[] objects) throws Exception {
        super.onWalkOn(roomUnit, room, objects);
    }

    @Override
    public void serializeExtradata(ServerMessage serverMessage) {
        serverMessage.appendInt((this.isLimited() ? 256 : 0));
        serverMessage.appendString(this.getExtradata());

        super.serializeExtradata(serverMessage);
    }

    public void updateState(Game game, int maxPlayers) {
        int memberCount = 0;

        if (game.getTeam(this.teamColor) != null) {
            memberCount = game.getTeam(this.teamColor).getMembers().size();
        }

        if (memberCount > maxPlayers) {
            memberCount = maxPlayers;
        }
        this.setExtradata(memberCount + "");
        game.getRoom().updateItem(this);
    }
}
