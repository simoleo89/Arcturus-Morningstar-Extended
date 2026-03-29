package com.eu.arcturus.habbohotel.wired.highscores;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredHighscore;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.util.HotelDateTimeUtil;
import java.util.HashSet;

import java.time.LocalTime;
import java.util.List;

public class WiredHighscoreMidnightUpdater implements Runnable {
    @Override
    public void run() {
        List<Room> rooms = Emulator.getGameEnvironment().getRoomManager().getActiveRooms();

        for (Room room : rooms) {
            if (room == null || room.getRoomSpecialTypes() == null) continue;

            HashSet<HabboItem> items = room.getRoomSpecialTypes().getItemsOfType(InteractionWiredHighscore.class);
            for (HabboItem item : items) {
                ((InteractionWiredHighscore) item).reloadData();
                room.updateItem(item);
            }
        }

        WiredHighscoreManager.midnightUpdater = Emulator.getThreading().run(new WiredHighscoreMidnightUpdater(), getNextUpdaterRun());
    }

    public static int getNextUpdaterRun() {
        long nextRunTimestamp = HotelDateTimeUtil.toEpochSecond(HotelDateTimeUtil.localDateTimeNow().with(LocalTime.MIDNIGHT).plusDays(1).plusSeconds(-1));
        return Math.toIntExact(nextRunTimestamp - Emulator.getIntUnixTimestamp()) + 5;
    }
}
