package com.eu.arcturus.plugin.events.games;

import com.eu.arcturus.habbohotel.games.Game;

public class GameStartedEvent extends GameEvent {

    public GameStartedEvent(Game game) {
        super(game);
    }
}
