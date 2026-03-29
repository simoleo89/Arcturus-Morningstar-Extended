package com.eu.habbo.messages.outgoing.rooms.items;

import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

import java.util.HashMap;
import java.util.HashSet;

import java.util.NoSuchElementException;
import java.util.Map;

public class RoomWallItemsComposer extends MessageComposer {
    private final Room room;

    public RoomWallItemsComposer(Room room) {
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomWallItemsComposer);
        Map<Integer, String> furniOwnerNames = this.room.getFurniOwnerNames();
        HashMap<Integer, String> userNames = new HashMap<>(furniOwnerNames);

        this.response.appendInt(userNames.size());
        for (Map.Entry<Integer, String> set : userNames.entrySet()) {
            this.response.appendInt(set.getKey());
            this.response.appendString(set.getValue());
        }

        HashSet<HabboItem> items = this.room.getWallItems();

        this.response.appendInt(items.size());
        for (HabboItem item : items) {
            item.serializeWallData(this.response);
        }
        return this.response;
    }

    public Room getRoom() {
        return room;
    }
}
