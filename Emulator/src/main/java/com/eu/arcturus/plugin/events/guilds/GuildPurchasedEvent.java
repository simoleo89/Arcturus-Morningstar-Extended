package com.eu.arcturus.plugin.events.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.users.Habbo;

public class GuildPurchasedEvent extends GuildEvent {

    public final Habbo habbo;


    public GuildPurchasedEvent(Guild guild, Habbo habbo) {
        super(guild);
        this.habbo = habbo;
    }
}
