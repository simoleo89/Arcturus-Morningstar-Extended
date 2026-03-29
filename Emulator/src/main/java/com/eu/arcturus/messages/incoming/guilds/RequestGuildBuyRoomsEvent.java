package com.eu.arcturus.messages.incoming.guilds;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.guilds.GuildBuyRoomsComposer;
import java.util.HashSet;

import java.util.List;

public class RequestGuildBuyRoomsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        List<Room> rooms = Emulator.getGameEnvironment().getRoomManager().getRoomsForHabbo(this.client.getHabbo());

        HashSet<Room> roomList = new HashSet<Room>();

        for (Room room : rooms) {
            if (room.getGuildId() == 0)
                roomList.add(room);
        }

        this.client.sendResponse(new GuildBuyRoomsComposer(roomList));
    }
}
