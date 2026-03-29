package com.eu.arcturus.messages.outgoing.rooms.items.youtube;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class YoutubeStateChangeComposer extends MessageComposer {
    private final int furniId;
    private final int state;

    public YoutubeStateChangeComposer(int furniId, int state) {
        this.furniId = furniId;
        this.state = state;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.YoutubeMessageComposer3);
        this.response.appendInt(this.furniId);
        this.response.appendInt(this.state);

        return this.response;
    }

    public int getFurniId() {
        return furniId;
    }

    public int getState() {
        return state;
    }
}
