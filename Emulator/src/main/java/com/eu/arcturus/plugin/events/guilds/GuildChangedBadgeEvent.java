package com.eu.arcturus.plugin.events.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;

public class GuildChangedBadgeEvent extends GuildEvent {

    public String badge;


    public GuildChangedBadgeEvent(Guild guild, String badge) {
        super(guild);

        this.badge = badge;
    }
}
