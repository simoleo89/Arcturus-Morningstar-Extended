package com.eu.arcturus.plugin.events.rooms;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.plugin.events.users.UserEvent;
import java.util.HashSet;

public class RoomFloorItemsLoadEvent extends UserEvent {
    private HashSet<HabboItem> floorItems;
    private boolean changedFloorItems;

    public RoomFloorItemsLoadEvent(Habbo habbo, HashSet<HabboItem> floorItems) {
        super(habbo);
        this.floorItems = floorItems;
        this.changedFloorItems = false;
    }

    public void setFloorItems(HashSet<HabboItem> floorItems) {
        this.changedFloorItems = true;
        this.floorItems = floorItems;
    }

    public boolean hasChangedFloorItems() {
        return this.changedFloorItems;
    }

    public HashSet<HabboItem> getFloorItems() {
        return this.floorItems;
    }
}
