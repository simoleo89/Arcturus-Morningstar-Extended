package com.eu.arcturus.threading.runnables;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.bots.Bot;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.wired.core.WiredManager;

public class BotFollowHabbo implements Runnable {
    private final Bot bot;
    private final Habbo habbo;
    private final Room room;
    private boolean hasReached;

    public BotFollowHabbo(Bot bot, Habbo habbo, Room room) {
        this.bot = bot;
        this.habbo = habbo;
        this.room = room;
        this.hasReached = false;
    }

    @Override
    public void run() {
        if (this.bot != null) {
            if (this.habbo != null && this.bot.getFollowingHabboId() == this.habbo.getHabboInfo().getId()) {
                if (this.habbo.getHabboInfo().getCurrentRoom() != null && this.habbo.getHabboInfo().getCurrentRoom() == this.room) {
                    if (this.habbo.getRoomUnit() != null) {
                        if (this.bot.getRoomUnit() != null) {
                            RoomTile target = this.room.getLayout().getTileInFront(this.habbo.getRoomUnit().getCurrentLocation(), Math.abs((this.habbo.getRoomUnit().getBodyRotation().getValue() + 4)) % 8);

                            if (target != null) {
                                if (target.x < 0 || target.y < 0)
                                    target = this.room.getLayout().getTileInFront(this.habbo.getRoomUnit().getCurrentLocation(), this.habbo.getRoomUnit().getBodyRotation().getValue());

                                if(this.habbo.getRoomUnit().getCurrentLocation().distance(this.bot.getRoomUnit().getCurrentLocation()) < 2) {
                                    if(!hasReached) {
                                        WiredManager.triggerBotReachedHabbo(room, bot.getRoomUnit(), habbo.getRoomUnit());
                                        hasReached = true;
                                    }
                                }
                                else {
                                    hasReached = false;
                                }

                                if (target.x >= 0 && target.y >= 0) {
                                    this.bot.getRoomUnit().setGoalLocation(target);
                                    this.bot.getRoomUnit().setCanWalk(true);
                                    Emulator.getThreading().run(this, 500);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
