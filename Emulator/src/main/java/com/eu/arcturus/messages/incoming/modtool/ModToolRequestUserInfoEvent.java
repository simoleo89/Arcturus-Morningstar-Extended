package com.eu.arcturus.messages.incoming.modtool;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.modtool.ModToolManager;
import com.eu.arcturus.habbohotel.modtool.ScripterManager;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class ModToolRequestUserInfoEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (this.client.getHabbo().hasPermission(Permission.ACC_SUPPORTTOOL)) {
            ModToolManager.requestUserInfo(this.client, this.packet);
        } else {
            ScripterManager.scripterDetected(this.client, Emulator.getTexts().getValue("scripter.warning.userinfo").replace("%username%", this.client.getHabbo().getHabboInfo().getUsername()));
        }
    }
}
