package com.eu.arcturus.plugin.events.bots;

import com.eu.arcturus.habbohotel.bots.Bot;
import com.eu.arcturus.plugin.Event;

public abstract class BotEvent extends Event {

    public final Bot bot;


    public BotEvent(Bot bot) {
        this.bot = bot;
    }
}
