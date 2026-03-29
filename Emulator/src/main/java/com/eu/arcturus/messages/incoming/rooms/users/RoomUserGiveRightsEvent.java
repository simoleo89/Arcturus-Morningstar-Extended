package com.eu.arcturus.messages.incoming.rooms.users;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.messenger.MessengerBuddy;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.plugin.events.users.UserRightsGivenEvent;

public class RoomUserGiveRightsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int userId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room == null)
            return;

        if (room.getOwnerId() == this.client.getHabbo().getHabboInfo().getId() || this.client.getHabbo().hasPermission(Permission.ACC_ANYROOMOWNER)) {
            Habbo target = room.getHabbo(userId);

            if (target != null) {
                if (!Emulator.getPluginManager().fireEvent(new UserRightsGivenEvent(this.client.getHabbo(), target)).isCancelled()) {
                    room.giveRights(target);
                }
            } else {
                MessengerBuddy buddy = this.client.getHabbo().getMessenger().getFriend(userId);

                if (buddy != null) {
                    room.giveRights(userId);
                }
            }
        }
    }
}
