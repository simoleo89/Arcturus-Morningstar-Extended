package com.eu.arcturus.threading.runnables.games;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.interactions.games.InteractionGameTimer;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.wired.core.WiredManager;

public class GameTimer implements Runnable {

    private final InteractionGameTimer timer;

    public GameTimer(InteractionGameTimer timer) {
        this.timer = timer;
    }

    @Override
    public void run() {
        if (timer.getRoomId() == 0) {
            timer.setRunning(false);
            return;
        }

        Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(timer.getRoomId());

        if (room == null || !timer.isRunning() || timer.isPaused()) {
            timer.setThreadActive(false);
            return;
        }

        timer.reduceTime();
        if (timer.getTimeNow() < 0) timer.setTimeNow(0);

        if (timer.getTimeNow() > 0) {
            timer.setThreadActive(true);
            Emulator.getThreading().run(this, 1000);
        } else {
            timer.setThreadActive(false);
            timer.setTimeNow(0);
            timer.endGame(room);
            WiredManager.triggerGameEnds(room);
        }

        room.updateItem(timer);
    }
}
