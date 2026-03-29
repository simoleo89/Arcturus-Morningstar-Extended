package com.eu.arcturus.messages.outgoing.rooms.users;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomUserRespectComposer extends MessageComposer {
    private final Habbo habbo;

    public RoomUserRespectComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomUserRespectComposer);
        this.response.appendInt(this.habbo.getHabboInfo().getId());
        this.response.appendInt(this.habbo.getHabboStats().respectPointsReceived);
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
