package com.eu.arcturus.messages.incoming.rooms.items;

import com.eu.arcturus.habbohotel.items.interactions.InteractionOneWayGate;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class TriggerOneWayGateEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (this.client.getHabbo().getHabboInfo().getCurrentRoom() == null)
            return;

        int itemId = this.packet.readInt();
        HabboItem item = this.client.getHabbo().getHabboInfo().getCurrentRoom().getHabboItem(itemId);

        if (item == null)
            return;

        if (item instanceof InteractionOneWayGate) {
            if (!item.getExtradata().equals("0") || this.client.getHabbo().getRoomUnit().isTeleporting)
                return;

            item.onClick(this.client, this.client.getHabbo().getHabboInfo().getCurrentRoom(), null);
        }

    }
}
