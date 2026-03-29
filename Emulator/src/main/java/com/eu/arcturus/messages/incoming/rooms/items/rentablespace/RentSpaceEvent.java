package com.eu.arcturus.messages.incoming.rooms.items.rentablespace;

import com.eu.arcturus.habbohotel.items.interactions.InteractionRentableSpace;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class RentSpaceEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int itemId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room == null)
            return;

        HabboItem item = room.getHabboItem(itemId);

        if (!(item instanceof InteractionRentableSpace))
            return;

        ((InteractionRentableSpace) item).rent(this.client.getHabbo());

        room.updateItem(item);

        ((InteractionRentableSpace) item).sendRentWidget(this.client.getHabbo());
    }
}
