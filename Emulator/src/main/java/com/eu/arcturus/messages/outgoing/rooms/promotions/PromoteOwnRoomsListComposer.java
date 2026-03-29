package com.eu.arcturus.messages.outgoing.rooms.promotions;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.ArrayList;
import java.util.List;

public class PromoteOwnRoomsListComposer extends MessageComposer {
    private final List<Room> rooms = new ArrayList<>();

    public PromoteOwnRoomsListComposer(List<Room> rooms) {
        for (Room room : rooms) {
            if (!room.isPromoted())
                this.rooms.add(room);
        }
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.PromoteOwnRoomsListComposer);
        this.response.appendBoolean(true);
        this.response.appendInt(this.rooms.size());
        for (Room room : this.rooms) {
            this.response.appendInt(room.getId());
            this.response.appendString(room.getName());
            this.response.appendBoolean(true); //IDK what the fuck this is.
        }
        return this.response;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
