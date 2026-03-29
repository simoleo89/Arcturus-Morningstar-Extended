package com.eu.arcturus.messages.incoming.modtool;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.modtool.ModToolIssue;
import com.eu.arcturus.habbohotel.modtool.ScripterManager;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class ModToolCloseTicketEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (this.client.getHabbo().hasPermission(Permission.ACC_SUPPORTTOOL)) {
            int state = this.packet.readInt();
            this.packet.readInt();
            int ticketId = this.packet.readInt();

            ModToolIssue issue = Emulator.getGameEnvironment().getModToolManager().getTicket(ticketId);

            if (issue == null || issue.modId != this.client.getHabbo().getHabboInfo().getId())
                return;

            Habbo sender = Emulator.getGameEnvironment().getHabboManager().getHabbo(issue.senderId);

            switch (state) {
                case 1:
                    Emulator.getGameEnvironment().getModToolManager().closeTicketAsUseless(issue, sender);
                    break;

                case 2:
                    Emulator.getGameEnvironment().getModToolManager().closeTicketAsAbusive(issue, sender);
                    break;

                case 3:
                    Emulator.getGameEnvironment().getModToolManager().closeTicketAsHandled(issue, sender);
                    break;
            }
        } else {
            ScripterManager.scripterDetected(this.client, Emulator.getTexts().getValue("scripter.warning.modtools.ticket.close").replace("%username%", this.client.getHabbo().getHabboInfo().getUsername()));
        }
    }
}
