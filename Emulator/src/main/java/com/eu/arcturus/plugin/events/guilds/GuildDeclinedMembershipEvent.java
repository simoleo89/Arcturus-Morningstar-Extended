package com.eu.arcturus.plugin.events.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.users.Habbo;

public class GuildDeclinedMembershipEvent extends GuildEvent {

    public final int userId;


    public final Habbo user;


    public final Habbo admin;


    public GuildDeclinedMembershipEvent(Guild guild, int userId, Habbo user, Habbo admin) {
        super(guild);

        this.userId = userId;
        this.user = user;
        this.admin = admin;
    }
}
