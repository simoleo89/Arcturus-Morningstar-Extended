package com.eu.arcturus.plugin.events.bots;

import com.eu.arcturus.habbohotel.bots.Bot;
import com.eu.arcturus.habbohotel.users.Habbo;

public class BotServerItemEvent extends BotEvent {

    public Habbo habbo;

    public int itemId;


    public BotServerItemEvent(Bot bot, Habbo habbo, int itemId) {
        super(bot);

        this.habbo = habbo;
        this.itemId = itemId;
    }
}