package com.eu.arcturus.messages.outgoing.rooms.users;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomUserDataComposer extends MessageComposer {
    private final Habbo habbo;

    public RoomUserDataComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomUserDataComposer);
        this.response.appendInt(this.habbo.getRoomUnit() == null ? -1 : this.habbo.getRoomUnit().getId());
        this.response.appendString(this.habbo.getHabboInfo().getLook());
        this.response.appendString(this.habbo.getHabboInfo().getGender().name() + "");
        this.response.appendString(this.habbo.getHabboInfo().getMotto());
        this.response.appendInt(this.habbo.getHabboStats().getAchievementScore());
        this.response.appendInt(this.habbo.getHabboInfo().getInfostandBg());
        this.response.appendInt(this.habbo.getHabboInfo().getInfostandStand());
        this.response.appendInt(this.habbo.getHabboInfo().getInfostandOverlay());
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
