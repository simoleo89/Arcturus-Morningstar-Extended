package com.eu.arcturus.plugin.events.bots;

import com.eu.arcturus.habbohotel.bots.Bot;

public class BotSavedNameEvent extends BotEvent {

    public String name;


    public BotSavedNameEvent(Bot bot, String name) {
        super(bot);

        this.name = name;
    }
}
