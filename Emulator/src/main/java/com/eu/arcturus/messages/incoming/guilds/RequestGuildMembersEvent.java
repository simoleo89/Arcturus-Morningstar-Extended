package com.eu.arcturus.messages.incoming.guilds;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.guilds.GuildMember;
import com.eu.arcturus.habbohotel.guilds.GuildRank;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.guilds.GuildMembersComposer;

public class RequestGuildMembersEvent extends MessageHandler {
    @Override
    public int getRatelimit() {
        return 500;
    }

    @Override
    public void handle() throws Exception {
        int groupId = this.packet.readInt();
        int pageId = this.packet.readInt();
        String query = this.packet.readString();
        int levelId = this.packet.readInt();

        Guild g = Emulator.getGameEnvironment().getGuildManager().getGuild(groupId);

        if (g != null) {
            boolean isAdmin = this.client.getHabbo().hasPermission(Permission.ACC_GUILD_ADMIN);
            if (!isAdmin && this.client.getHabbo().getHabboStats().hasGuild(g.getId())) {
                GuildMember member = Emulator.getGameEnvironment().getGuildManager().getGuildMember(g, this.client.getHabbo());
                isAdmin = member != null && (member.getRank().equals(GuildRank.OWNER) || member.getRank().equals(GuildRank.ADMIN));
            }

            this.client.sendResponse(new GuildMembersComposer(g, Emulator.getGameEnvironment().getGuildManager().getGuildMembers(g, pageId, levelId, query), this.client.getHabbo(), pageId, levelId, query, isAdmin, Emulator.getGameEnvironment().getGuildManager().getGuildMembersCount(g, pageId, levelId, query)));
        }
    }
}
