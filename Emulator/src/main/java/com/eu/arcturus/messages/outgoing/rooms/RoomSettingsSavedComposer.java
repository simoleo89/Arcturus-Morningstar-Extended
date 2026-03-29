package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomSettingsSavedComposer extends MessageComposer {
    private final Room room;

    public RoomSettingsSavedComposer(Room room) {
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomSettingsSavedComposer);
        this.response.appendInt(this.room.getId());
        return this.response;
    }

    public Room getRoom() {
        return room;
    }
}
