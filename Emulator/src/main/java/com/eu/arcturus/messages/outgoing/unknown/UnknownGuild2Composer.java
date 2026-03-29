package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class UnknownGuild2Composer extends MessageComposer {
    private final int guildId;

    public UnknownGuild2Composer(int guildId) {
        this.guildId = guildId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UnknownGuild2Composer);
        this.response.appendInt(this.guildId);
        return this.response;
    }

    public int getGuildId() {
        return guildId;
    }
}