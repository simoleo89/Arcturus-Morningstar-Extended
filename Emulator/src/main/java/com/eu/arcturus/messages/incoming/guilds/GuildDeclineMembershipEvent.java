package com.eu.arcturus.messages.incoming.guilds;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.guilds.GuildMember;
import com.eu.arcturus.habbohotel.guilds.GuildRank;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.guilds.GuildInfoComposer;
import com.eu.arcturus.messages.outgoing.guilds.GuildMembersComposer;
import com.eu.arcturus.messages.outgoing.guilds.GuildRefreshMembersListComposer;
import com.eu.arcturus.plugin.events.guilds.GuildDeclinedMembershipEvent;

public class GuildDeclineMembershipEvent extends MessageHandler {
    @Override
    public int getRatelimit() {
        return 500;
    }

    @Override
    public void handle() throws Exception {
        int guildId = this.packet.readInt();
        int userId = this.packet.readInt();

        Guild guild = Emulator.getGameEnvironment().getGuildManager().getGuild(guildId);

        if (guild != null) {
            GuildMember member = Emulator.getGameEnvironment().getGuildManager().getGuildMember(guild, this.client.getHabbo());
            if (userId == this.client.getHabbo().getHabboInfo().getId() || guild.getOwnerId() == this.client.getHabbo().getHabboInfo().getId() || (member != null && (member.getRank().equals(GuildRank.ADMIN) || member.getRank().equals(GuildRank.OWNER))) || this.client.getHabbo().hasPermission(Permission.ACC_GUILD_ADMIN)) {
                guild.decreaseRequestCount();
                Emulator.getGameEnvironment().getGuildManager().removeMember(guild, userId);
                this.client.sendResponse(new GuildMembersComposer(guild, Emulator.getGameEnvironment().getGuildManager().getGuildMembers(guild, 0, 0, ""), this.client.getHabbo(), 0, 0, "", true, Emulator.getGameEnvironment().getGuildManager().getGuildMembersCount(guild, 0, 0, "")));
                this.client.sendResponse(new GuildRefreshMembersListComposer(guild));

                Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(userId);
                Emulator.getPluginManager().fireEvent(new GuildDeclinedMembershipEvent(guild, userId, habbo, this.client.getHabbo()));

                if (habbo != null) {
                    Room room = habbo.getHabboInfo().getCurrentRoom();
                    if (room != null) {
                        if (room.getGuildId() == guildId) {
                            habbo.getClient().sendResponse(new GuildInfoComposer(guild, habbo.getClient(), false, null));
                        }
                    }
                }
            }
        }
    }
}
