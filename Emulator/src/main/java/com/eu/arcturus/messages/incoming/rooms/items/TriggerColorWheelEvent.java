package com.eu.arcturus.messages.incoming.rooms.items;

import com.eu.arcturus.habbohotel.items.interactions.InteractionColorWheel;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class TriggerColorWheelEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int itemId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room == null)
            return;

        HabboItem item = room.getHabboItem(itemId);

        if (item instanceof InteractionColorWheel) {
            item.onClick(this.client, room, null);
        }
    }
}
