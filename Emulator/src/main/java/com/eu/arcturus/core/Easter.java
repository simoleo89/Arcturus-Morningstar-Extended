package com.eu.arcturus.core;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessage;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserRemoveComposer;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserWhisperComposer;
import com.eu.arcturus.plugin.EventHandler;
import com.eu.arcturus.plugin.events.users.UserSavedMottoEvent;

public class Easter {
    @EventHandler
    public static void onUserChangeMotto(UserSavedMottoEvent event) {
        if (Emulator.getConfig().getBoolean("easter_eggs.enabled") && event.newMotto.equalsIgnoreCase("crickey!")) {
            event.habbo.getClient().sendResponse(new RoomUserWhisperComposer(new RoomChatMessage(event.newMotto, event.habbo, event.habbo, RoomChatMessageBubbles.ALERT)));

            Room room = event.habbo.getHabboInfo().getCurrentRoom();

            room.sendComposer(new RoomUserRemoveComposer(event.habbo.getRoomUnit()).compose());
            room.sendComposer(new RoomUserPetComposer(2, 1, "FFFFFF", event.habbo).compose());

        }
    }
}
