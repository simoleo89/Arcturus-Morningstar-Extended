package com.eu.arcturus.messages.incoming.rooms.items;

import com.eu.arcturus.habbohotel.items.interactions.games.football.InteractionFootballGate;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class FootballGateSaveLookEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room == null || this.client.getHabbo().getHabboInfo().getId() != room.getOwnerId())
            return;

        HabboItem item = room.getHabboItem(this.packet.readInt());
        if (!(item instanceof InteractionFootballGate))
            return;

        String gender = this.packet.readString();
        String look = this.packet.readString();

        switch (gender.toLowerCase()) {
            default:
            case "m":
                ((InteractionFootballGate) item).setFigureM(look);
                room.updateItem(item);
                break;

            case "f":
                ((InteractionFootballGate) item).setFigureF(look);
                room.updateItem(item);
                break;
        }
    }
}