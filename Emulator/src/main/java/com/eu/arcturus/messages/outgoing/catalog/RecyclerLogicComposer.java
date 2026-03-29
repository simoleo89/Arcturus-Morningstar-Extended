package com.eu.arcturus.messages.outgoing.catalog;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;
import java.util.HashSet;

import java.util.Map;

public class RecyclerLogicComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RecyclerLogicComposer);
        this.response.appendInt(Emulator.getGameEnvironment().getCatalogManager().prizes.size());
        for (Map.Entry<Integer, HashSet<Item>> map : Emulator.getGameEnvironment().getCatalogManager().prizes.entrySet()) {
            this.response.appendInt(map.getKey());
            this.response.appendInt(Integer.valueOf(Emulator.getConfig().getValue("hotel.ecotron.rarity.chance." + map.getKey())));
            this.response.appendInt(map.getValue().size());
            for (Item item : map.getValue()) {
                this.response.appendString(item.getName());
                this.response.appendInt(1);
                this.response.appendString(item.getType().code.toLowerCase());
                this.response.appendInt(item.getSpriteId());
            }
        }
        return this.response;
    }
}
