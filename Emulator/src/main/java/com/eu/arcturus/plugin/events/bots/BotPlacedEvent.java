package com.eu.arcturus.plugin.events.bots;

import com.eu.arcturus.habbohotel.bots.Bot;
import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.habbohotel.users.Habbo;

public class BotPlacedEvent extends BotEvent {

    public final RoomTile location;


    public final Habbo placer;

    public BotPlacedEvent(Bot bot, RoomTile location, Habbo placer) {
        super(bot);

        this.location = location;
        this.placer = placer;
    }
}
