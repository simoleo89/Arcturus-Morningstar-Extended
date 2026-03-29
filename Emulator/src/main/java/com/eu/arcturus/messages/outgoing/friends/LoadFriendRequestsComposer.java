package com.eu.arcturus.messages.outgoing.friends;

import com.eu.arcturus.habbohotel.messenger.FriendRequest;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class LoadFriendRequestsComposer extends MessageComposer {
    private final Habbo habbo;

    public LoadFriendRequestsComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.LoadFriendRequestsComposer);

        synchronized (this.habbo.getMessenger().getFriendRequests()) {
            this.response.appendInt(this.habbo.getMessenger().getFriendRequests().size());
            this.response.appendInt(this.habbo.getMessenger().getFriendRequests().size());

            for (FriendRequest friendRequest : this.habbo.getMessenger().getFriendRequests()) {
                this.response.appendInt(friendRequest.getId());
                this.response.appendString(friendRequest.getUsername());
                this.response.appendString(friendRequest.getLook());
            }
        }

        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}