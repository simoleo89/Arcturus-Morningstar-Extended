package com.eu.habbo.messages.outgoing.rooms;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomBan;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

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
