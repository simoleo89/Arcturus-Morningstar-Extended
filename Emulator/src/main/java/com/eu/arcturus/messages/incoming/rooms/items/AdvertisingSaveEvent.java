package com.eu.arcturus.messages.incoming.rooms.items;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.interactions.InteractionCustomValues;
import com.eu.arcturus.habbohotel.items.interactions.InteractionRoomAds;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;
import java.util.HashMap;

public class AdvertisingSaveEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();
        if (room == null)
            return;

        if (!room.hasRights(this.client.getHabbo()))
            return;

        HabboItem item = room.getHabboItem(this.packet.readInt());
        if (item == null)
            return;

        if (item instanceof InteractionRoomAds && !this.client.getHabbo().hasPermission("acc_ads_background")) {
            this.client.getHabbo().alert(Emulator.getTexts().getValue("hotel.error.roomads.nopermission"));
            return;
        }
        if (item instanceof InteractionCustomValues) {
            HashMap<String, String> oldValues = new HashMap<>(((InteractionCustomValues) item).values);
            int count = this.packet.readInt();
            for (int i = 0; i < count / 2; i++) {
                String key = this.packet.readString();
                String value = this.packet.readString();

                if (!Emulator.getConfig().getBoolean("camera.use.https")) {
                    value = value.replace("https://", "http://");
                }

                ((InteractionCustomValues) item).values.put(key, value);
            }

            item.setExtradata(((InteractionCustomValues) item).toExtraData());
            item.needsUpdate(true);
            Emulator.getThreading().run(item);
            room.updateItem(item);
            ((InteractionCustomValues) item).onCustomValuesSaved(room, this.client, oldValues);
        }
    }
}
