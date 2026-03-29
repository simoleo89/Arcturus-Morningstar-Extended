package com.eu.arcturus.plugin.events.bots;

import com.eu.arcturus.habbohotel.bots.Bot;

public abstract class BotChatEvent extends BotEvent {

    public String message;


    public BotChatEvent(Bot bot, String message) {
        super(bot);

        this.message = message;
    }
}
