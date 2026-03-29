package com.eu.arcturus.messages.incoming.rooms.users;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.items.interactions.wired.triggers.WiredTriggerHabboClicksUser;
import com.eu.arcturus.habbohotel.wired.core.WiredManager;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.users.InClientLinkComposer;

public class ClickUserEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room == null) {
            return;
        }

        RoomUnit clickingUser = this.client.getHabbo().getRoomUnit();

        if (clickingUser == null) {
            return;
        }

        int roomUnitId = this.packet.readInt();
        Habbo clickedHabbo = room.getHabboByRoomUnitId(roomUnitId);

        if (clickedHabbo == null || clickedHabbo.getRoomUnit() == null) {
            return;
        }

        WiredManager.triggerUserClicksUser(room, clickingUser, clickedHabbo.getRoomUnit());

        if (WiredTriggerHabboClicksUser.hasPendingIgnoreLook(clickingUser)) {
            this.client.sendResponse(new InClientLinkComposer("avatar-info/block-rotate"));
        }

        if (WiredTriggerHabboClicksUser.consumeBlockMenuOpen(clickingUser)) {
            this.client.sendResponse(new InClientLinkComposer("avatar-info/block-menu"));
        }
    }
}
