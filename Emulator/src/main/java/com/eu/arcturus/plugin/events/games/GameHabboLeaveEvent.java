package com.eu.arcturus.plugin.events.games;

import com.eu.arcturus.habbohotel.games.Game;
import com.eu.arcturus.habbohotel.users.Habbo;

public class GameHabboLeaveEvent extends GameUserEvent {

    public GameHabboLeaveEvent(Game game, Habbo habbo) {
        super(game, habbo);
    }
}
