package com.eu.arcturus.messages.incoming.ambassadors;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.modtool.ScripterManager;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.generic.alerts.BubbleAlertComposer;
import com.eu.arcturus.plugin.events.support.SupportUserAlertedEvent;
import com.eu.arcturus.plugin.events.support.SupportUserAlertedReason;

public class AmbassadorAlertCommandEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (!this.client.getHabbo().hasPermission(Permission.ACC_AMBASSADOR)) {
            ScripterManager.scripterDetected(this.client, Emulator.getTexts().getValue("scripter.warning.modtools.alert").replace("%username%", client.getHabbo().getHabboInfo().getUsername()).replace("%message%", "${notification.ambassador.alert.warning.message}"));
            return;
        }

        int userId = this.packet.readInt();

        Habbo habbo = this.client.getHabbo().getHabboInfo().getCurrentRoom().getHabbo(userId);

        if (habbo == null)
            return;

        SupportUserAlertedEvent alertedEvent = new SupportUserAlertedEvent(client.getHabbo(), habbo, "${notification.ambassador.alert.warning.message}", SupportUserAlertedReason.AMBASSADOR);

        if (Emulator.getPluginManager().fireEvent(alertedEvent).isCancelled())
            return;

        habbo.getClient().sendResponse(new BubbleAlertComposer("ambassador.alert.warning"));
    }
}
