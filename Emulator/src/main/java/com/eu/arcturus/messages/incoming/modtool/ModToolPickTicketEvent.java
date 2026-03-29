package com.eu.arcturus.messages.incoming.modtool;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.modtool.ModToolIssue;
import com.eu.arcturus.habbohotel.modtool.ModToolTicketState;
import com.eu.arcturus.habbohotel.modtool.ScripterManager;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.modtool.ModToolIssueInfoComposer;

public class ModToolPickTicketEvent extends MessageHandler {
    public static boolean send = false;

    @Override
    public void handle() throws Exception {
        if (this.client.getHabbo().hasPermission(Permission.ACC_SUPPORTTOOL)) {
            this.packet.readInt();
            ModToolIssue issue = Emulator.getGameEnvironment().getModToolManager().getTicket(this.packet.readInt());

            if (issue != null) {
                if (issue.state == ModToolTicketState.PICKED) {
                    this.client.sendResponse(new ModToolIssueInfoComposer(issue));
                    this.client.getHabbo().alert(Emulator.getTexts().getValue("support.ticket.picked.failed"));

                    return;
                }

                //this.client.sendResponse(new ModToolIssueInfoComposer(issue));
                Emulator.getGameEnvironment().getModToolManager().pickTicket(issue, this.client.getHabbo());
            } else {
                this.client.getHabbo().alert(Emulator.getTexts().getValue("support.ticket.picked.failed"));
            }
        } else {
            ScripterManager.scripterDetected(this.client, Emulator.getTexts().getValue("scripter.warning.modtools.ticket.pick").replace("%username%", this.client.getHabbo().getHabboInfo().getUsername()));
        }
    }
}
