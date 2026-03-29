package com.eu.arcturus.habbohotel.rooms;

import com.eu.arcturus.habbohotel.users.Habbo;
import java.util.HashSet;

/**
 * Manages trading operations within a room.
 */
public class RoomTradeManager {
    private final Room room;
    private final HashSet<RoomTrade> activeTrades;

    public RoomTradeManager(Room room) {
        this.room = room;
        this.activeTrades = new HashSet<>(0);
    }

    /**
     * Starts a trade between two users.
     */
    public void startTrade(Habbo userOne, Habbo userTwo) {
        RoomTrade trade = new RoomTrade(userOne, userTwo, this.room);
        synchronized (this.activeTrades) {
            this.activeTrades.add(trade);
        }

        trade.start();
    }

    /**
     * Stops a trade.
     */
    public void stopTrade(RoomTrade trade) {
        synchronized (this.activeTrades) {
            this.activeTrades.remove(trade);
        }
    }

    /**
     * Gets the active trade for a user.
     */
    public RoomTrade getActiveTradeForHabbo(Habbo user) {
        synchronized (this.activeTrades) {
            for (RoomTrade trade : this.activeTrades) {
                for (RoomTradeUser habbo : trade.getRoomTradeUsers()) {
                    if (habbo.getHabbo() == user) {
                        return trade;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets all active trades.
     */
    public HashSet<RoomTrade> getActiveTrades() {
        return this.activeTrades;
    }
}
