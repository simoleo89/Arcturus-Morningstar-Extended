package com.eu.arcturus.plugin.events.games;

import com.eu.arcturus.habbohotel.games.Game;
import com.eu.arcturus.plugin.Event;

public abstract class GameEvent extends Event {

    public final Game game;


    public GameEvent(Game game) {
        this.game = game;
    }
}
