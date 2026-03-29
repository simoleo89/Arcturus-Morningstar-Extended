package com.eu.arcturus.messages.incoming.rooms.promotions;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.promotions.PromoteOwnRoomsListComposer;

import java.util.List;

public class RequestPromotionRoomsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        List<Room> roomsToShow = Emulator.getGameEnvironment().getRoomManager().getRoomsForHabbo(this.client.getHabbo());
        roomsToShow.addAll(Emulator.getGameEnvironment().getRoomManager().getRoomsWithRights(this.client.getHabbo()));
        roomsToShow.addAll(Emulator.getGameEnvironment().getRoomManager().getRoomsWithAdminRights(this.client.getHabbo()));
        this.client.sendResponse(new PromoteOwnRoomsListComposer(roomsToShow));
    }
}
