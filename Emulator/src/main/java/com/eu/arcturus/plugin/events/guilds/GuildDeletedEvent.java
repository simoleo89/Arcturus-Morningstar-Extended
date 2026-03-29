package com.eu.arcturus.plugin.events.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.users.Habbo;

public class GuildDeletedEvent extends GuildEvent {

    public final Habbo deleter;


    public GuildDeletedEvent(Guild guild, Habbo deleter) {
        super(guild);

        this.deleter = deleter;
    }
}
