package com.eu.arcturus.messages.incoming.guilds.forums;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.guilds.GuildMember;
import com.eu.arcturus.habbohotel.guilds.GuildRank;
import com.eu.arcturus.habbohotel.guilds.forums.ForumThread;
import com.eu.arcturus.habbohotel.guilds.forums.ForumThreadState;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.generic.alerts.BubbleAlertComposer;
import com.eu.arcturus.messages.outgoing.generic.alerts.BubbleAlertKeys;
import com.eu.arcturus.messages.outgoing.guilds.forums.GuildForumThreadMessagesComposer;
import com.eu.arcturus.messages.outgoing.guilds.forums.GuildForumThreadsComposer;
import com.eu.arcturus.messages.outgoing.handshake.ConnectionErrorComposer;


public class GuildForumModerateThreadEvent extends MessageHandler {
    @Override
    public int getRatelimit() {
        return 500;
    }

    @Override
    public void handle() throws Exception {
        int guildId = packet.readInt();
        int threadId = packet.readInt();
        int state = packet.readInt();
        // STATE 20 - HIDDEN_BY_GUILD_ADMIN = HIDDEN BY GUILD ADMINS/ HOTEL MODERATORS
        // STATE 1 = VISIBLE THREAD

        Guild guild = Emulator.getGameEnvironment().getGuildManager().getGuild(guildId);
        ForumThread thread = ForumThread.getById(threadId);

        if (guild == null || thread == null) {
            this.client.sendResponse(new ConnectionErrorComposer(404));
            return;
        }

        GuildMember member = Emulator.getGameEnvironment().getGuildManager().getGuildMember(guildId, this.client.getHabbo().getHabboInfo().getId());
        boolean hasStaffPerms = this.client.getHabbo().hasPermission(Permission.ACC_MODTOOL_TICKET_Q);

        if (member == null && !hasStaffPerms && guild.getOwnerId() != this.client.getHabbo().getHabboInfo().getId()) {
            this.client.sendResponse(new ConnectionErrorComposer(401));
            return;
        }

        boolean isGuildAdmin = (guild.getOwnerId() == this.client.getHabbo().getHabboInfo().getId() || (member != null && member.getRank().equals(GuildRank.ADMIN)));

        if (!isGuildAdmin && !hasStaffPerms) {
            this.client.sendResponse(new ConnectionErrorComposer(403));
            return;
        }

        thread.setState(ForumThreadState.fromValue(state)); // sets state as defined in the packet
        thread.run();

        switch (state) {
            case 10:
            case 20:
                this.client.sendResponse(new BubbleAlertComposer(BubbleAlertKeys.FORUMS_THREAD_HIDDEN.key).compose());
                break;
            case 1:
                this.client.sendResponse(new BubbleAlertComposer(BubbleAlertKeys.FORUMS_THREAD_RESTORED.key).compose());
                break;
        }

        this.client.sendResponse(new GuildForumThreadMessagesComposer(thread));
        this.client.sendResponse(new GuildForumThreadsComposer(guild, 0));
    }
}