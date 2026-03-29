package com.eu.arcturus.habbohotel.items.interactions.games.battlebanzai.gates;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.games.Game;
import com.eu.arcturus.habbohotel.games.GameState;
import com.eu.arcturus.habbohotel.games.GameTeam;
import com.eu.arcturus.habbohotel.games.GameTeamColors;
import com.eu.arcturus.habbohotel.games.battlebanzai.BattleBanzaiGame;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.items.interactions.games.InteractionGameGate;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractionBattleBanzaiGate extends InteractionGameGate {
    public InteractionBattleBanzaiGate(ResultSet set, Item baseItem, GameTeamColors teamColor) throws SQLException {
        super(set, baseItem, teamColor);
    }

    public InteractionBattleBanzaiGate(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells, GameTeamColors teamColor) {
        super(id, userId, item, extradata, limitedStack, limitedSells, teamColor);
    }

    @Override
    public boolean canWalkOn(RoomUnit roomUnit, Room room, Object[] objects) {
        return room.getGame(BattleBanzaiGame.class) == null || ((BattleBanzaiGame) room.getGame(BattleBanzaiGame.class)).state.equals(GameState.IDLE);
    }

    @Override
    public void onWalk(RoomUnit roomUnit, Room room, Object[] objects) throws Exception {

    }

    @Override
    public boolean isWalkable() {
        Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(this.getRoomId());

        if (room == null) return false;

        Game game = room.getGame(BattleBanzaiGame.class);

        return game == null || game.getState() == GameState.IDLE;
    }

    @Override
    public void onWalkOn(RoomUnit roomUnit, Room room, Object[] objects) throws Exception {
        BattleBanzaiGame game = (BattleBanzaiGame) room.getGame(BattleBanzaiGame.class);

        if (game == null) {
            game = BattleBanzaiGame.class.getDeclaredConstructor(Room.class).newInstance(room);
            room.addGame(game);
        }

        GameTeam team = game.getTeamForHabbo(room.getHabbo(roomUnit));

        if (team != null) {
            game.removeHabbo(room.getHabbo(roomUnit));
        } else {
            game.addHabbo(room.getHabbo(roomUnit), this.teamColor);
        }

        updateState(game, 5);

        super.onWalkOn(roomUnit, room, objects);
    }
}
