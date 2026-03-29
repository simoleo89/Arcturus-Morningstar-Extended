package com.eu.arcturus.messages.incoming.modtool;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guilds.forums.ForumThread;
import com.eu.arcturus.habbohotel.modtool.CfhTopic;
import com.eu.arcturus.habbohotel.modtool.ModToolIssue;
import com.eu.arcturus.habbohotel.modtool.ModToolTicketType;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.modtool.ModToolReportReceivedAlertComposer;
import com.eu.arcturus.threading.runnables.InsertModToolIssue;

public class ReportThreadEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int groupId = this.packet.readInt();
        int threadId = this.packet.readInt();
        int topicId = this.packet.readInt();
        String message = this.packet.readString();

        CfhTopic topic = Emulator.getGameEnvironment().getModToolManager().getCfhTopic(topicId);

        if (topic == null) return;

        ForumThread thread = ForumThread.getById(threadId);

        if (thread == null) return;

        Habbo opener = Emulator.getGameEnvironment().getHabboManager().getHabbo(thread.getOpenerId());

        ModToolIssue issue = new ModToolIssue(this.client.getHabbo().getHabboInfo().getId(), this.client.getHabbo().getHabboInfo().getUsername(), thread.getOpenerId(), opener == null ? "" : opener.getHabboInfo().getUsername(), 0, message, ModToolTicketType.DISCUSSION);
        issue.category = topicId;
        issue.groupId = groupId;
        issue.threadId = threadId;
        new InsertModToolIssue(issue).run();

        this.client.sendResponse(new ModToolReportReceivedAlertComposer(ModToolReportReceivedAlertComposer.REPORT_RECEIVED, message));
        Emulator.getGameEnvironment().getModToolManager().addTicket(issue);
        Emulator.getGameEnvironment().getModToolManager().updateTicketToMods(issue);
    }
}
