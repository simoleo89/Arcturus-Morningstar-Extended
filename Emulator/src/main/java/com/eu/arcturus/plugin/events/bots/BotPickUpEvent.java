package com.eu.arcturus.plugin.events.bots;

import com.eu.arcturus.habbohotel.bots.Bot;
import com.eu.arcturus.habbohotel.users.Habbo;

public class BotPickUpEvent extends BotEvent {

    public final Habbo picker;


    public BotPickUpEvent(Bot bot, Habbo picker) {
        super(bot);

        this.picker = picker;
    }
}
