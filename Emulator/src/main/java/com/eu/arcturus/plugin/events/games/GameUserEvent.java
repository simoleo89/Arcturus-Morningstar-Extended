package com.eu.arcturus.plugin.events.games;

import com.eu.arcturus.habbohotel.games.Game;
import com.eu.arcturus.habbohotel.users.Habbo;

public abstract class GameUserEvent extends GameEvent {

    public final Habbo habbo;


    public GameUserEvent(Game game, Habbo habbo) {
        super(game);

        this.habbo = habbo;
    }
}
