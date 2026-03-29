package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomBan;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.HashSet;

import java.util.NoSuchElementException;

public class RoomBannedUsersComposer extends MessageComposer {
    private final Room room;

    public RoomBannedUsersComposer(Room room) {
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        int timeStamp = Emulator.getIntUnixTimestamp();

        HashSet<RoomBan> roomBans = new HashSet<>();

        for (RoomBan roomBan : this.room.getBannedHabbos().values()) {
            if (roomBan.endTimestamp > timeStamp)
                roomBans.add(roomBan);
        }

        if (roomBans.isEmpty())
            return null;

        this.response.init(Outgoing.RoomBannedUsersComposer);
        this.response.appendInt(this.room.getId());
        this.response.appendInt(roomBans.size());

        for (RoomBan ban : roomBans) {
            this.response.appendInt(ban.userId);
            this.response.appendString(ban.username);
        }

        return this.response;
    }

    public Room getRoom() {
        return room;
    }
}
