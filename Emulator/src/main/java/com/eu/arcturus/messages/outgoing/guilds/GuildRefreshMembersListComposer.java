package com.eu.arcturus.messages.outgoing.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuildRefreshMembersListComposer extends MessageComposer {
    private final Guild guild;

    public GuildRefreshMembersListComposer(Guild guild) {
        this.guild = guild;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuildRefreshMembersListComposer);
        this.response.appendInt(this.guild.getId());
        this.response.appendInt(0);
        return this.response;
    }

    public Guild getGuild() {
        return guild;
    }
}
