package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class FavoriteRoomChangedComposer extends MessageComposer {
    private final int roomId;
    private final boolean favorite;

    public FavoriteRoomChangedComposer(int roomId, boolean favorite) {
        this.roomId = roomId;
        this.favorite = favorite;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.FavoriteRoomChangedComposer);
        this.response.appendInt(this.roomId);
        this.response.appendBoolean(this.favorite);
        return this.response;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean isFavorite() {
        return favorite;
    }
}