package com.eu.arcturus.messages.outgoing.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuildBoughtComposer extends MessageComposer {
    private final Guild guild;

    public GuildBoughtComposer(Guild guild) {
        this.guild = guild;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuildBoughtComposer);
        this.response.appendInt(this.guild.getRoomId());
        this.response.appendInt(this.guild.getId());
        return this.response;
    }

    public Guild getGuild() {
        return guild;
    }
}
