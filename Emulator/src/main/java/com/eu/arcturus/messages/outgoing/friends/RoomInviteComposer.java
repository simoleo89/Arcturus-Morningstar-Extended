package com.eu.arcturus.messages.outgoing.friends;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomInviteComposer extends MessageComposer {
    private final int userId;
    private final String message;

    public RoomInviteComposer(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomInviteComposer);
        this.response.appendInt(this.userId);
        this.response.appendString(this.message);
        return this.response;
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}
