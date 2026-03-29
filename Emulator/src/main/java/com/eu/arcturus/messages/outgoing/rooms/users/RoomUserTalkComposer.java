package com.eu.arcturus.messages.outgoing.rooms.users;

import com.eu.arcturus.habbohotel.rooms.RoomChatMessage;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomUserTalkComposer extends MessageComposer {
    private final RoomChatMessage roomChatMessage;

    public RoomUserTalkComposer(RoomChatMessage roomChatMessage) {
        this.roomChatMessage = roomChatMessage;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomUserTalkComposer);

        if (this.roomChatMessage.getMessage().isEmpty())
            return null;

        this.roomChatMessage.serialize(this.response);
        return this.response;
    }

    public RoomChatMessage getRoomChatMessage() {
        return roomChatMessage;
    }
}
