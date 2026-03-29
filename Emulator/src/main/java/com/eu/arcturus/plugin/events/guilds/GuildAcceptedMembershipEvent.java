package com.eu.arcturus.plugin.events.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.users.Habbo;

public class GuildAcceptedMembershipEvent extends GuildEvent {

    public final int userId;


    public final Habbo user;


    public GuildAcceptedMembershipEvent(Guild guild, int userId, Habbo user) {
        super(guild);

        this.userId = userId;
        this.user = user;
    }
}
