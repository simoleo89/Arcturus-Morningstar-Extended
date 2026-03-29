package com.eu.arcturus.messages.outgoing.navigator;

import com.eu.arcturus.habbohotel.rooms.RoomCategory;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.List;

public class NewNavigatorCategoryUserCountComposer extends MessageComposer {
    public final List<RoomCategory> roomCategories;

    public NewNavigatorCategoryUserCountComposer(List<RoomCategory> roomCategories) {
        this.roomCategories = roomCategories;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.NewNavigatorCategoryUserCountComposer);
        this.response.appendInt(this.roomCategories.size());

        for (RoomCategory category : this.roomCategories) {
            this.response.appendInt(category.getId());
            this.response.appendInt(0);
            this.response.appendInt(200);
        }
        return this.response;
    }

    public List<RoomCategory> getRoomCategories() {
        return roomCategories;
    }
}