package com.eu.arcturus.messages.incoming.rooms.items.rentablespace;

import com.eu.arcturus.habbohotel.items.interactions.InteractionRentableSpace;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class RentSpaceCancelEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int itemId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room == null)
            return;

        HabboItem item = room.getHabboItem(itemId);

        if (room.getOwnerId() == this.client.getHabbo().getHabboInfo().getId() ||
                this.client.getHabbo().hasPermission(Permission.ACC_ANYROOMOWNER)) {
            if (item instanceof InteractionRentableSpace) {
                ((InteractionRentableSpace) item).endRent();

                room.updateItem(item);
            }
        }
    }
}
