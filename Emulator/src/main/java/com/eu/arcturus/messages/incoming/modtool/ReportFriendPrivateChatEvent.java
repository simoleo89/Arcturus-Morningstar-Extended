package com.eu.arcturus.messages.incoming.modtool;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.modtool.ModToolChatLog;
import com.eu.arcturus.habbohotel.modtool.ModToolIssue;
import com.eu.arcturus.habbohotel.modtool.ModToolTicketType;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboInfo;
import com.eu.arcturus.habbohotel.users.HabboManager;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.modtool.HelperRequestDisabledComposer;
import com.eu.arcturus.threading.runnables.InsertModToolIssue;

import java.util.ArrayList;

public class ReportFriendPrivateChatEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (!this.client.getHabbo().getHabboStats().allowTalk()) {
            this.client.sendResponse(new HelperRequestDisabledComposer());
            return;
        }

        String message = this.packet.readString();
        int category = this.packet.readInt();
        int userId = this.packet.readInt();
        int count = this.packet.readInt();
        ArrayList<ModToolChatLog> chatLogs = new ArrayList<>();

        HabboInfo info;
        Habbo target = Emulator.getGameEnvironment().getHabboManager().getHabbo(userId);
        if (target != null) {
            info = target.getHabboInfo();
        } else {
            info = HabboManager.getOfflineHabboInfo(userId);
        }

        if (info == null) return;

        for (int i = 0; i < Math.min(count, 100); i++) {
            int chatUserId = this.packet.readInt();
            String username = this.packet.readInt() == info.getId() ? info.getUsername() : this.client.getHabbo().getHabboInfo().getUsername();

            chatLogs.add(new ModToolChatLog(0, chatUserId, username, this.packet.readString()));
        }

        ModToolIssue issue = new ModToolIssue(this.client.getHabbo().getHabboInfo().getId(), this.client.getHabbo().getHabboInfo().getUsername(), userId, info.getUsername(), 0, message, ModToolTicketType.IM);
        issue.category = category;
        issue.chatLogs = chatLogs;
        new InsertModToolIssue(issue).run();
        Emulator.getGameEnvironment().getModToolManager().addTicket(issue);
        Emulator.getGameEnvironment().getModToolManager().updateTicketToMods(issue);
    }
}
