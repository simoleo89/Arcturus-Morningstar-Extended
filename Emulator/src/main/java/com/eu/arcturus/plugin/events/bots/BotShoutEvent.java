package com.eu.arcturus.plugin.events.bots;

import com.eu.arcturus.habbohotel.bots.Bot;

public class BotShoutEvent extends BotChatEvent {

    public BotShoutEvent(Bot bot, String message) {
        super(bot, message);
    }
}
