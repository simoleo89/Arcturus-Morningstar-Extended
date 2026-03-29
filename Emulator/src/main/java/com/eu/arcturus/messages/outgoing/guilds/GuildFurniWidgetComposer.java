package com.eu.arcturus.messages.outgoing.guilds;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuildFurniWidgetComposer extends MessageComposer {
    private final HabboItem item;
    private final Guild guild;
    private final Habbo habbo;

    public GuildFurniWidgetComposer(Habbo habbo, Guild guild, HabboItem item) {
        this.habbo = habbo;
        this.item = item;
        this.guild = guild;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuildFurniWidgetComposer);
        this.response.appendInt(item.getId());
        this.response.appendInt(this.guild.getId());
        this.response.appendString(this.guild.getName());
        this.response.appendInt(this.guild.getRoomId());
        this.response.appendBoolean(Emulator.getGameEnvironment().getGuildManager().getGuildMember(this.guild, this.habbo) != null); //User Joined.
        this.response.appendBoolean(this.guild.hasForum()); //Has Forum.
        return this.response;
    }

    public HabboItem getItem() {
        return item;
    }

    public Guild getGuild() {
        return guild;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
