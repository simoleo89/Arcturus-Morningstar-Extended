package com.eu.habbo.messages.outgoing.friends;

import com.eu.habbo.habbohotel.messenger.MessengerBuddy;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;
import java.util.function.Consumer;
import java.util.HashSet;

public class RoomInviteErrorComposer extends MessageComposer {
    private final int errorCode;
    private final HashSet<MessengerBuddy> buddies;

    public RoomInviteErrorComposer(int errorCode, HashSet<MessengerBuddy> buddies) {
        this.errorCode = errorCode;
        this.buddies = buddies;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomInviteErrorComposer);
        this.response.appendInt(this.errorCode);
        this.response.appendInt(this.buddies.size());
        this.buddies.forEach(new Consumer<MessengerBuddy>() {
            @Override
            public void accept(MessengerBuddy object) {
                RoomInviteErrorComposer.this.response.appendInt(object.getId());
            }
        });
        return this.response;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HashSet<MessengerBuddy> getBuddies() {
        return buddies;
    }
}