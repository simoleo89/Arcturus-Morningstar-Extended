package com.eu.arcturus.messages.incoming.users;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.plugin.events.users.UserSavedSettingsEvent;

public class SaveIgnoreRoomInvitesEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.getHabbo().getHabboStats().blockRoomInvites = this.packet.readBoolean();
        Emulator.getPluginManager().fireEvent(new UserSavedSettingsEvent(this.client.getHabbo()));
    }
}
