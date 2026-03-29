package com.eu.arcturus.messages.incoming.users;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboManager;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.users.UserProfileComposer;

public class RequestUserProfileEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int habboId = this.packet.readInt();
        Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(habboId);

        if (habbo != null)
            this.client.sendResponse(new UserProfileComposer(habbo, this.client));
        else
            this.client.sendResponse(new UserProfileComposer(HabboManager.getOfflineHabboInfo(habboId), this.client));
    }
}
