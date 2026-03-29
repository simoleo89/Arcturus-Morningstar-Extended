package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.rooms.RoomChatMessage;
import com.eu.arcturus.habbohotel.rooms.RoomChatType;
import com.eu.arcturus.habbohotel.users.Habbo;

public class UserTalkEvent extends UserEvent {
    public final RoomChatMessage chatMessage;
    public final RoomChatType chatType;

    public UserTalkEvent(Habbo habbo, RoomChatMessage chatMessage, RoomChatType chatType) {
        super(habbo);
        this.chatMessage = chatMessage;
        this.chatType = chatType;
    }
}