package com.eu.arcturus.plugin.events.bots;

import com.eu.arcturus.habbohotel.bots.Bot;

public class BotTalkEvent extends BotChatEvent {

    public BotTalkEvent(Bot bot, String message) {
        super(bot, message);
    }
}
