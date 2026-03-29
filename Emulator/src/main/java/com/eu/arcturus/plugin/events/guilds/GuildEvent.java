package com.eu.arcturus.plugin.events.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.plugin.Event;

public abstract class GuildEvent extends Event {

    public final Guild guild;


    public GuildEvent(Guild guild) {
        this.guild = guild;
    }
}
