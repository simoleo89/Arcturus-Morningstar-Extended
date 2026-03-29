package com.eu.arcturus.messages.outgoing.guilds;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RemoveGuildFromRoomComposer extends MessageComposer {
    private int guildId;

    public RemoveGuildFromRoomComposer(int guildId) {
        this.guildId = guildId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RemoveGuildFromRoomComposer);
        this.response.appendInt(this.guildId);
        return this.response;
    }

    public int getGuildId() {
        return guildId;
    }
}
