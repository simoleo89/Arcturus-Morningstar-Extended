package com.eu.arcturus.plugin.events.games;

import com.eu.arcturus.habbohotel.games.Game;

public class GameStoppedEvent extends GameEvent {

    public GameStoppedEvent(Game game) {
        super(game);
    }
}
