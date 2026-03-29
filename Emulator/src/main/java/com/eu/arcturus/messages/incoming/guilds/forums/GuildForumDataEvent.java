package com.eu.arcturus.messages.incoming.guilds.forums;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.guilds.forums.GuildForumDataComposer;

public class GuildForumDataEvent extends MessageHandler {
    @Override
    public int getRatelimit() {
        return 500;
    }

    @Override
    public void handle() throws Exception {
        int guildId = packet.readInt();

        Guild guild = Emulator.getGameEnvironment().getGuildManager().getGuild(guildId);

        if (guild == null) return;
        if (!guild.hasForum()) return;

        this.client.sendResponse(new GuildForumDataComposer(guild, this.client.getHabbo()));

        if (!Emulator.getGameEnvironment().getGuildManager().hasViewedForum(this.client.getHabbo().getHabboInfo().getId(), guildId)) {
            Emulator.getGameEnvironment().getGuildManager().addView(this.client.getHabbo().getHabboInfo().getId(), guildId);
        }
    }
}