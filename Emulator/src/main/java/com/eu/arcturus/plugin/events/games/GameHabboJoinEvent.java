package com.eu.arcturus.plugin.events.games;

import com.eu.arcturus.habbohotel.games.Game;
import com.eu.arcturus.habbohotel.users.Habbo;

public class GameHabboJoinEvent extends GameUserEvent {

    public GameHabboJoinEvent(Game game, Habbo habbo) {
        super(game, habbo);
    }
}
