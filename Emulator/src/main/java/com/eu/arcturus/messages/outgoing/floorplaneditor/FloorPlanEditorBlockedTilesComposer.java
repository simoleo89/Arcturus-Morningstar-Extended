package com.eu.arcturus.messages.outgoing.floorplaneditor;

import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;
import java.util.HashSet;

public class FloorPlanEditorBlockedTilesComposer extends MessageComposer {
    private final Room room;

    public FloorPlanEditorBlockedTilesComposer(Room room) {
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.FloorPlanEditorBlockedTilesComposer);

        HashSet<RoomTile> tileList = this.room.getLockedTiles();

        this.response.appendInt(tileList.size());
        for (RoomTile node : tileList) {
            this.response.appendInt((int) node.x);
            this.response.appendInt((int) node.y);
        }

        return this.response;
    }

    public Room getRoom() {
        return room;
    }
}
