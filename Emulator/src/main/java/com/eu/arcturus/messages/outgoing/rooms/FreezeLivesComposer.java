package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.habbohotel.games.freeze.FreezeGamePlayer;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class FreezeLivesComposer extends MessageComposer {
    private final FreezeGamePlayer gamePlayer;

    public FreezeLivesComposer(FreezeGamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.FreezeLivesComposer);
        this.response.appendInt(this.gamePlayer.getHabbo().getRoomUnit().getId());
        this.response.appendInt(this.gamePlayer.getLives());
        return this.response;
    }

    public FreezeGamePlayer getGamePlayer() {
        return gamePlayer;
    }
}
