package com.eu.arcturus.plugin.events.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.users.Habbo;

public class GuildFavoriteSetEvent extends GuildEvent {

    public final Habbo habbo;


    public GuildFavoriteSetEvent(Guild guild, Habbo habbo) {
        super(guild);

        this.habbo = habbo;
    }
}
