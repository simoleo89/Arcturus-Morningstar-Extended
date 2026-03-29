package com.eu.arcturus.messages.incoming.ambassadors;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.ForwardToRoomComposer;

public class AmbassadorVisitCommandEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (this.client.getHabbo().hasPermission(Permission.ACC_AMBASSADOR)) {
            String username = this.packet.readString();

            Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(username);

            if (habbo != null) {
                if (habbo.getHabboInfo().getCurrentRoom() != null) {
                    this.client.sendResponse(new ForwardToRoomComposer(habbo.getHabboInfo().getCurrentRoom().getId()));
                }
            }
        }
    }
}
