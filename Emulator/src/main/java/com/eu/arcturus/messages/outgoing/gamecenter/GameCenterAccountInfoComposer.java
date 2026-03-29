package com.eu.arcturus.messages.outgoing.gamecenter;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GameCenterAccountInfoComposer extends MessageComposer {
    private final int gameId;
    private final int gamesLeft;

    public GameCenterAccountInfoComposer(int gameId, int gamesLeft) {
        this.gameId = gameId;
        this.gamesLeft = gamesLeft;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GameCenterAccountInfoComposer);
        this.response.appendInt(this.gameId);
        this.response.appendInt(this.gamesLeft);
        this.response.appendInt(1);
        return this.response;
    }

    public int getGameId() {
        return gameId;
    }

    public int getGamesLeft() {
        return gamesLeft;
    }
}