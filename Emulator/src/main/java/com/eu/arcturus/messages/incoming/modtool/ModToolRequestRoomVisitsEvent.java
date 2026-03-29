package com.eu.arcturus.messages.incoming.modtool;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.users.HabboInfo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.modtool.ModToolUserRoomVisitsComposer;

public class ModToolRequestRoomVisitsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (this.client.getHabbo().hasPermission(Permission.ACC_SUPPORTTOOL)) {
            int userId = this.packet.readInt();

            HabboInfo habboInfo = Emulator.getGameEnvironment().getHabboManager().getHabboInfo(userId);

            if (habboInfo != null) {
                this.client.sendResponse(new ModToolUserRoomVisitsComposer(habboInfo, Emulator.getGameEnvironment().getModToolManager().getUserRoomVisits(userId)));
            }
        }
    }
}
