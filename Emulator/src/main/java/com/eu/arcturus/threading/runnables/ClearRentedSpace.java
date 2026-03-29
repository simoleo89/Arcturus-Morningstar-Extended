package com.eu.arcturus.threading.runnables;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.interactions.InteractionRentableSpace;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.outgoing.inventory.AddHabboItemComposer;
import java.util.HashSet;

public class ClearRentedSpace implements Runnable {
    private final InteractionRentableSpace item;
    private final Room room;

    public ClearRentedSpace(InteractionRentableSpace item, Room room) {
        this.item = item;
        this.room = room;
    }

    @Override
    public void run() {
        HashSet<HabboItem> items = new HashSet<>();

        for (RoomTile t : this.room.getLayout().getTilesAt(this.room.getLayout().getTile(this.item.getX(), this.item.getY()), this.item.getBaseItem().getWidth(), this.item.getBaseItem().getLength(), this.item.getRotation())) {
            for (HabboItem i : this.room.getItemsAt(t)) {
                if (i.getUserId() == this.item.getRenterId()) {
                    items.add(i);
                    i.setRoomId(0);
                    i.needsUpdate(true);
                }
            }
        }

        Habbo owner = Emulator.getGameEnvironment().getHabboManager().getHabbo(this.item.getRenterId());

        if (owner != null) {
            owner.getClient().sendResponse(new AddHabboItemComposer(items));
            owner.getHabboStats().rentedItemId = 0;
            owner.getHabboStats().rentedTimeEnd = 0;
        } else {
            for (HabboItem i : items) {
                i.run();
            }
        }
    }
}
