package com.eu.arcturus.threading.runnables;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;

public class LoadCustomHeightMap implements Runnable {
    private final Room room;

    public LoadCustomHeightMap(Room room) {
        this.room = room;
    }

    @Override
    public void run() {
        this.room.setLayout(Emulator.getGameEnvironment().getRoomManager().loadCustomLayout(this.room));
    }
}
