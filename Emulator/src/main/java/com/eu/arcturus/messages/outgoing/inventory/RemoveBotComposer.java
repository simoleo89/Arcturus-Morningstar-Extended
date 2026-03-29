package com.eu.arcturus.messages.outgoing.inventory;

import com.eu.arcturus.habbohotel.bots.Bot;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RemoveBotComposer extends MessageComposer {
    private final Bot bot;

    public RemoveBotComposer(Bot bot) {
        this.bot = bot;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RemoveBotComposer);
        this.response.appendInt(this.bot.getId());
        return this.response;
    }

    public Bot getBot() {
        return bot;
    }
}
