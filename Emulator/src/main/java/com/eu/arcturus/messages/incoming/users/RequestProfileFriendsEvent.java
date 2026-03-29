package com.eu.arcturus.messages.incoming.users;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.messenger.Messenger;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.users.ProfileFriendsComposer;

public class RequestProfileFriendsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int userId = this.packet.readInt();
        Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(userId);

        if (habbo != null)
            this.client.sendResponse(new ProfileFriendsComposer(habbo));
        else
            this.client.sendResponse(new ProfileFriendsComposer(Messenger.getFriends(userId), userId));
    }
}
