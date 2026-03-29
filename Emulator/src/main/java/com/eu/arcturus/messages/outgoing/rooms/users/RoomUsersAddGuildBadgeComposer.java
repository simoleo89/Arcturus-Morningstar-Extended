package com.eu.arcturus.messages.outgoing.rooms.users;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomUsersAddGuildBadgeComposer extends MessageComposer {
    private final Guild guild;

    public RoomUsersAddGuildBadgeComposer(Guild guild) {
        this.guild = guild;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomUsersGuildBadgesComposer);
        this.response.appendInt(1);
        this.response.appendInt(this.guild.getId());
        this.response.appendString(this.guild.getBadge());
        return this.response;
    }

    public Guild getGuild() {
        return guild;
    }
}