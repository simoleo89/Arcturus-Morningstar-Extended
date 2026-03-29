package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomFilterWordsComposer extends MessageComposer {
    private final Room room;

    public RoomFilterWordsComposer(Room room) {
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomFilterWordsComposer);

        this.response.appendInt(this.room.getWordFilterWords().size());

        for (String string : this.room.getWordFilterWords()) {
            this.response.appendString(string);
        }

        return this.response;
    }

    public Room getRoom() {
        return room;
    }
}
