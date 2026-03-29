package com.eu.arcturus.threading.runnables.hopper;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;

class HopperActionFour implements Runnable {
    private final HabboItem currentTeleport;
    private final Room room;
    private final GameClient client;

    public HopperActionFour(HabboItem currentTeleport, Room room, GameClient client) {
        this.currentTeleport = currentTeleport;
        this.client = client;
        this.room = room;
    }

    @Override
    public void run() {
        this.currentTeleport.setExtradata("1");
        this.room.updateItem(this.currentTeleport);

        Emulator.getThreading().run(new HopperActionFive(this.currentTeleport, this.room, this.client), 500);
    }
}
