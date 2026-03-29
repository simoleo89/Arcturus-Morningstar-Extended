package com.eu.arcturus.messages.incoming.guilds;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.guilds.GuildInfoComposer;

public class RequestGuildInfoEvent extends MessageHandler {
    @Override
    public int getRatelimit() {
        return 500;
    }

    @Override
    public void handle() throws Exception {
        int guildId = this.packet.readInt();
        boolean newWindow = this.packet.readBoolean();

        Guild guild = Emulator.getGameEnvironment().getGuildManager().getGuild(guildId);

        if (guild != null) {
            this.client.sendResponse(new GuildInfoComposer(guild, this.client, newWindow, Emulator.getGameEnvironment().getGuildManager().getGuildMember(guild, this.client.getHabbo())));
        }
    }
}
