package com.eu.arcturus.plugin.events.bots;

import com.eu.arcturus.habbohotel.bots.Bot;
import com.eu.arcturus.habbohotel.users.Habbo;

public class BotWhisperEvent extends BotChatEvent {

    public Habbo target;


    public BotWhisperEvent(Bot bot, String message, Habbo target) {
        super(bot, message);

        this.target = target;
    }
}
