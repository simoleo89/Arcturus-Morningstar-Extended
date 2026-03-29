package com.eu.arcturus.messages.incoming.rooms.items;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.interactions.InteractionDice;
import com.eu.arcturus.habbohotel.modtool.ScripterManager;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomLayout;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class CloseDiceEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int itemId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room == null)
            return;

        HabboItem item = room.getHabboItem(itemId);

        if (item != null) {
            if (item instanceof InteractionDice) {
                if (RoomLayout.tilesAdjecent(room.getLayout().getTile(item.getX(), item.getY()), this.client.getHabbo().getRoomUnit().getCurrentLocation())) {
                    if (!item.getExtradata().equals("-1")) {
                        item.setExtradata("0");
                        item.needsUpdate(true);
                        Emulator.getThreading().run(item);
                        room.updateItem(item);
                    }
                }
            } else {
                ScripterManager.scripterDetected(this.client, Emulator.getTexts().getValue("scripter.warning.packet.closedice").replace("%username%", this.client.getHabbo().getHabboInfo().getUsername()).replace("%id%", item.getId() + "").replace("%itemname%", item.getBaseItem().getName()));
            }
        }
    }
}
