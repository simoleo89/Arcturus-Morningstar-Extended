package com.eu.habbo.messages.outgoing.rooms.items;

import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomConfInvisSupport;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public class ConfInvisStateComposer extends MessageComposer {
    private final int roomId;
    private final boolean active;
    private final IntArrayList hiddenItemIds;

    public ConfInvisStateComposer(Room room) {
        this.roomId = (room != null) ? room.getId() : 0;
        this.active = RoomConfInvisSupport.hasActiveController(room);
        this.hiddenItemIds = RoomConfInvisSupport.collectHiddenFloorItemIds(room);
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.ConfInvisStateComposer);
        this.response.appendInt(this.roomId);
        this.response.appendBoolean(this.active);
        this.response.appendInt(this.hiddenItemIds.size());

        for (int i = 0; i < this.hiddenItemIds.size(); i++) {
            this.response.appendInt(this.hiddenItemIds.getInt(i));
        }

        return this.response;
    }
}
