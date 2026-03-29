package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomScoreComposer extends MessageComposer {
    private final int score;
    private final boolean canVote;

    public RoomScoreComposer(int score, boolean canVote) {
        this.score = score;
        this.canVote = canVote;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomScoreComposer);
        this.response.appendInt(this.score);
        this.response.appendBoolean(this.canVote);
        return this.response;
    }

    public int getScore() {
        return score;
    }

    public boolean isCanVote() {
        return canVote;
    }
}
