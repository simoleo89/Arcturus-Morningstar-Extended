package com.eu.habbo.messages.outgoing.friends;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class FriendTypingComposer extends MessageComposer {
    private final int senderId;
    private final boolean isTyping;

    public FriendTypingComposer(int senderId, boolean isTyping) {
        this.senderId = senderId;
        this.isTyping = isTyping;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.FriendTypingComposer);
        this.response.appendInt(this.senderId);
        this.response.appendBoolean(this.isTyping);
        return this.response;
    }
}
