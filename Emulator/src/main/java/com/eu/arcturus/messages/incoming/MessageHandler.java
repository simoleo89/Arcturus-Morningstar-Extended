package com.eu.arcturus.messages.incoming;

import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ClientMessage;

public abstract class MessageHandler {
    public GameClient client;
    public ClientMessage packet;
    public boolean isCancelled = false;

    public abstract void handle() throws Exception;

    public int getRatelimit() {
        return 0;
    }

    protected final Room currentRoom() {
        if (this.client == null || this.client.getHabbo() == null) return null;
        if (this.client.getHabbo().getHabboInfo() == null) return null;
        return this.client.getHabbo().getHabboInfo().getCurrentRoom();
    }
}