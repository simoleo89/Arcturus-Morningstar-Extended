package com.eu.arcturus.plugin.events.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.users.Habbo;

public class GuildGivenAdminEvent extends GuildEvent {

    public final int userId;


    public final Habbo habbo;


    public final Habbo admin;


    public GuildGivenAdminEvent(Guild guild, int userId, Habbo habbo, Habbo admin) {
        super(guild);
        this.userId = userId;
        this.habbo = habbo;
        this.admin = admin;
    }
}
