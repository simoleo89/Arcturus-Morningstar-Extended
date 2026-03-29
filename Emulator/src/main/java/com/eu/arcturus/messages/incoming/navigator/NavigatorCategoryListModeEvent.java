package com.eu.arcturus.messages.incoming.navigator;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.navigation.ListMode;
import com.eu.arcturus.habbohotel.rooms.RoomCategory;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class NavigatorCategoryListModeEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String category = this.packet.readString();
        int viewMode = this.packet.readInt();

        RoomCategory roomCategory = Emulator.getGameEnvironment().getRoomManager().getCategory(category);
        this.client.getHabbo().getHabboStats().navigatorWindowSettings.setListMode(
                roomCategory != null ? roomCategory.getCaptionSave() : category, viewMode == 1 ? ListMode.THUMBNAILS : ListMode.LIST);
    }
}
