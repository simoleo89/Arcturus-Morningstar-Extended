package com.eu.arcturus.messages.incoming.navigator;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.RoomCategory;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.navigator.RoomCategoriesComposer;

import java.util.List;

public class RequestRoomCategoriesEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        List<RoomCategory> roomCategoryList = Emulator.getGameEnvironment().getRoomManager().roomCategoriesForHabbo(this.client.getHabbo());
        this.client.sendResponse(new RoomCategoriesComposer(roomCategoryList));
        //this.client.sendResponse(new NewNavigatorEventCategoriesComposer());
    }
}
