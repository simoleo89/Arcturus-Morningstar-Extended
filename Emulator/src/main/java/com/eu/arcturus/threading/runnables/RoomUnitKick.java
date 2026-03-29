package com.eu.arcturus.threading.runnables;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;

public class RoomUnitKick implements Runnable {
    private final Habbo habbo;
    private final Room room;
    private final boolean removeEffect;

    public RoomUnitKick(Habbo habbo, Room room, boolean removeEffect) {
        this.habbo = habbo;
        this.room = room;
        this.removeEffect = removeEffect;
    }

    @Override
    public void run() {
        if (this.removeEffect) {
            this.habbo.getRoomUnit().setEffectId(0, 0);
        }

        Emulator.getGameEnvironment().getRoomManager().leaveRoom(this.habbo, this.room);
    }
}
