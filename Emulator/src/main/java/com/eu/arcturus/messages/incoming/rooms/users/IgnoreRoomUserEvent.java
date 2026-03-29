package com.eu.arcturus.messages.incoming.rooms.users;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.achievements.AchievementManager;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserIgnoredComposer;

public class IgnoreRoomUserEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room != null) {
            String username = this.packet.readString();

            Habbo habbo = room.getHabbo(username);

            if (habbo != null) {
                if (habbo == this.client.getHabbo())
                    return;

                if (this.client.getHabbo().getHabboStats().ignoreUser(this.client, habbo.getHabboInfo().getId())) {
                    this.client.sendResponse(new RoomUserIgnoredComposer(habbo, RoomUserIgnoredComposer.IGNORED));
                    AchievementManager.progressAchievement(this.client.getHabbo(), Emulator.getGameEnvironment().getAchievementManager().getAchievement("SelfModIgnoreSeen"));
                }
            }
        }
    }
}
