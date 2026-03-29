package com.eu.arcturus.messages.incoming.rooms;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class RoomVoteEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Emulator.getGameEnvironment().getRoomManager().voteForRoom(this.client.getHabbo(), this.client.getHabbo().getHabboInfo().getCurrentRoom());
    }
}
