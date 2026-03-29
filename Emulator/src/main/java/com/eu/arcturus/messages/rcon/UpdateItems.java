package com.eu.arcturus.messages.rcon;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.outgoing.rooms.RoomRelativeMapComposer;
import com.google.gson.Gson;

public class UpdateItems extends RCONMessage<UpdateItems.JSONUpdateItems> {

    public UpdateItems() {
        super(JSONUpdateItems.class);
    }

    @Override
    public void handle(Gson gson, JSONUpdateItems json) {
        Emulator.getGameEnvironment().getItemManager().loadItems();
        Emulator.getGameEnvironment().getItemManager().loadCrackable();
        Emulator.getGameEnvironment().getItemManager().loadSoundTracks();

        synchronized (Emulator.getGameEnvironment().getRoomManager().getActiveRooms()) {
            for (Room room : Emulator.getGameEnvironment().getRoomManager().getActiveRooms()) {
                if (room.isLoaded() && room.getUserCount() > 0 && room.getLayout() != null) {
                    room.sendComposer(new RoomRelativeMapComposer(room).compose());
                }
            }
        }
    }

    static class JSONUpdateItems {
    }
}