package com.eu.habbo.messages.outgoing.rooms.users;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class RoomUsersGuildBadgesComposer extends MessageComposer {
    private final HashMap<Integer, String> guildBadges;

    public RoomUsersGuildBadgesComposer(HashMap<Integer, String> guildBadges) {
        this.guildBadges = guildBadges;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomUsersGuildBadgesComposer);
        this.response.appendInt(this.guildBadges.size());

        this.guildBadges.forEach(new BiConsumer<Integer, String>() {
            @Override
            public void accept(Integer guildId, String badge) {
                RoomUsersGuildBadgesComposer.this.response.appendInt(guildId);
                RoomUsersGuildBadgesComposer.this.response.appendString(badge);
            }
        });
        return this.response;
    }

    public HashMap<Integer, String> getGuildBadges() {
        return guildBadges;
    }
}