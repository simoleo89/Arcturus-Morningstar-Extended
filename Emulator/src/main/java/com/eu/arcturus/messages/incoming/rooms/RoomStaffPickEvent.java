package com.eu.arcturus.messages.incoming.rooms;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.achievements.AchievementManager;
import com.eu.arcturus.habbohotel.navigation.NavigatorPublicCategory;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.RoomDataComposer;

public class RoomStaffPickEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (this.client.getHabbo().hasPermission(Permission.ACC_STAFF_PICK)) {
            int roomId = this.packet.readInt();

            Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(roomId);

            if (room != null) {
                room.setStaffPromotedRoom(!room.isStaffPromotedRoom());
                room.setNeedsUpdate(true);

                NavigatorPublicCategory publicCategory = Emulator.getGameEnvironment().getNavigatorManager().publicCategories.get(Emulator.getConfig().getInt("hotel.navigator.staffpicks.categoryid"));
                if (room.isStaffPromotedRoom()) {
                    Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(room.getOwnerId());

                    if (habbo != null) {
                        AchievementManager.progressAchievement(habbo, Emulator.getGameEnvironment().getAchievementManager().getAchievement("Spr"));
                    }

                    if (publicCategory != null) {
                        publicCategory.addRoom(room);
                    }
                } else {
                    if (publicCategory != null) {
                        publicCategory.removeRoom(room);
                    }
                }

                this.client.sendResponse(new RoomDataComposer(room, this.client.getHabbo(), true, false));
            }
        }
    }
}
