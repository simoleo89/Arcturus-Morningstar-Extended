package com.eu.arcturus.messages.outgoing.rooms.users;

import com.eu.arcturus.habbohotel.rooms.RoomChatMessage;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomUserShoutComposer extends MessageComposer {
    private RoomChatMessage roomChatMessage;

    public RoomUserShoutComposer(RoomChatMessage roomChatMessage) {
        this.roomChatMessage = roomChatMessage;
    }

    @Override
    protected ServerMessage composeInternal() {
        if (this.roomChatMessage.getMessage().isEmpty())
            return null;

        this.response.init(Outgoing.RoomUserShoutComposer);
        this.roomChatMessage.serialize(this.response);

        return this.response;
    }

    public RoomChatMessage getRoomChatMessage() {
        return roomChatMessage;
    }
}
