package com.eu.arcturus.messages.outgoing.inventory;

import com.eu.arcturus.habbohotel.bots.Bot;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;
import java.util.HashMap;

public class InventoryBotsComposer extends MessageComposer {
    private final Habbo habbo;

    public InventoryBotsComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.InventoryBotsComposer);

        HashMap<Integer, Bot> userBots = this.habbo.getInventory().getBotsComponent().getBots();
        this.response.appendInt(userBots.size());
        for (Bot bot : userBots.values()) {
            this.response.appendInt(bot.getId());
            this.response.appendString(bot.getName());
            this.response.appendString(bot.getMotto());
            this.response.appendString(bot.getGender().toString().toLowerCase().charAt(0) + "");
            this.response.appendString(bot.getFigure());
        }

        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
