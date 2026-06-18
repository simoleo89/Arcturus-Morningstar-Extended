package com.eu.habbo.messages.outgoing.rooms.users;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;
import java.util.HashMap;
import java.util.Map;

public class RoomUsersGuildBadgesComposer extends MessageComposer {
    private final HashMap<Integer, String> guildBadges;

    public RoomUsersGuildBadgesComposer(HashMap<Integer, String> guildBadges) {
        this.guildBadges = guildBadges;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomUsersGuildBadgesComposer);
        this.response.appendInt(this.guildBadges.size());

        for (Map.Entry<Integer, String> entry : this.guildBadges.entrySet()) {
            this.response.appendInt(entry.getKey());
            this.response.appendString(entry.getValue());
        }
        return this.response;
    }

    public HashMap<Integer, String> getGuildBadges() {
        return guildBadges;
    }
}