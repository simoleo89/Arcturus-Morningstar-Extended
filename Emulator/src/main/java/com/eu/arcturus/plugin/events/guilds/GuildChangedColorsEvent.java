package com.eu.arcturus.plugin.events.guilds;

import com.eu.arcturus.habbohotel.guilds.Guild;

public class GuildChangedColorsEvent extends GuildEvent {

    public int colorOne;


    public int colorTwo;


    public GuildChangedColorsEvent(Guild guild, int colorOne, int colorTwo) {
        super(guild);

        this.colorOne = colorOne;
        this.colorTwo = colorTwo;
    }
}
