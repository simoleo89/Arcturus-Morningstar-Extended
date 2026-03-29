package com.eu.arcturus.messages.outgoing.friends;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;
import java.util.ArrayList;

public class RemoveFriendComposer extends MessageComposer {
    private final ArrayList<Integer> unfriendIds;

    public RemoveFriendComposer(ArrayList<Integer> unfriendIds) {
        this.unfriendIds = unfriendIds;
    }

    public RemoveFriendComposer(int i) {
        this.unfriendIds = new ArrayList<>();
        this.unfriendIds.add(i);
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UpdateFriendComposer);

        this.response.appendInt(0);
        this.response.appendInt(this.unfriendIds.size());
        for (int i = 0; i < this.unfriendIds.size(); i++) {
            this.response.appendInt(-1);
            this.response.appendInt(this.unfriendIds.get(i));
        }

        return this.response;
    }

    public ArrayList<Integer> getUnfriendIds() {
        return unfriendIds;
    }
}
