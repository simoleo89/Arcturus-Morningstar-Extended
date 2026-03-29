package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.rooms.RoomChatMessage;
import com.eu.arcturus.habbohotel.rooms.RoomChatType;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;

public class UsernameTalkEvent extends UserEvent {
    public final RoomChatMessage chatMessage;
    public final RoomChatType chatType;

    private ServerMessage customComposer = null;

    public UsernameTalkEvent(Habbo habbo, RoomChatMessage chatMessage, RoomChatType chatType) {
        super(habbo);
        this.chatMessage = chatMessage;
        this.chatType = chatType;
    }

    public void setCustomComposer(ServerMessage customComposer) {
        this.customComposer = customComposer;
    }

    public boolean hasCustomComposer() {
        return this.customComposer != null;
    }

    public ServerMessage getCustomComposer() {
        return this.customComposer;
    }
}
