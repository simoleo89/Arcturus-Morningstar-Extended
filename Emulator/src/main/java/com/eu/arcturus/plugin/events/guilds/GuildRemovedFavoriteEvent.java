package com.eu.arcturus.plugin.events.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.users.Habbo;

public class GuildRemovedFavoriteEvent extends GuildEvent {

    public final Habbo habbo;


    public GuildRemovedFavoriteEvent(Guild guild, Habbo habbo) {
        super(guild);
        this.habbo = habbo;
    }
}
