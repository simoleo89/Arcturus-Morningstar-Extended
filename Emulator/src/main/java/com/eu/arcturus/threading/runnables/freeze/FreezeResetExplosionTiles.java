package com.eu.arcturus.threading.runnables.freeze;

import com.eu.arcturus.habbohotel.items.interactions.games.freeze.InteractionFreezeTile;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;
import java.util.HashSet;

class FreezeResetExplosionTiles implements Runnable {
    private final HashSet<InteractionFreezeTile> tiles;
    private final Room room;

    public FreezeResetExplosionTiles(HashSet<InteractionFreezeTile> tiles, Room room) {
        this.tiles = tiles;
        this.room = room;
    }

    @Override
    public void run() {
        for (HabboItem item : this.tiles) {
            item.setExtradata("0");
            this.room.updateItem(item);
        }
    }
}
