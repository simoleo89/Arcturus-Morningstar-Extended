package com.eu.arcturus.threading.runnables.teleport;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.items.interactions.InteractionTeleportTile;
import com.eu.arcturus.habbohotel.rooms.*;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.habbohotel.wired.core.WiredFreezeUtil;
import com.eu.arcturus.threading.runnables.HabboItemNewState;
import com.eu.arcturus.threading.runnables.RoomUnitWalkToLocation;

import java.util.ArrayList;
import java.util.List;

class TeleportActionFive implements Runnable {
    private final HabboItem currentTeleport;
    private final Room room;
    private final GameClient client;

    public TeleportActionFive(HabboItem currentTeleport, Room room, GameClient client) {
        this.currentTeleport = currentTeleport;
        this.client = client;
        this.room = room;
    }

    @Override
    public void run() {
        RoomUnit unit = this.client.getHabbo().getRoomUnit();

        unit.isLeavingTeleporter = false;
        unit.isTeleporting = false;
        unit.setCanWalk(true);

        if (this.client.getHabbo().getHabboInfo().getCurrentRoom() != this.room) {
            this.client.getHabbo().getHabboInfo().setLoadingRoom(0);
            return;
        }

        //if (!(this.currentTeleport instanceof InteractionTeleportTile))

        if (this.room.getLayout() == null || this.currentTeleport == null) return;

        RoomTile currentLocation = this.room.getLayout().getTile(this.currentTeleport.getX(), this.currentTeleport.getY());
        RoomTile tile = this.room.getLayout().getTileInFront(currentLocation, this.currentTeleport.getRotation());

        if (tile != null) {
            List<Runnable> onSuccess = new ArrayList<Runnable>();
            onSuccess.add(() -> {
                unit.setCanLeaveRoomByDoor(true);
                WiredFreezeUtil.restoreWalkState(unit);

                Emulator.getThreading().run(() -> {
                    unit.isLeavingTeleporter = false;
                }, 300);
            });

            unit.setCanLeaveRoomByDoor(false);
            unit.setGoalLocation(tile);
            unit.statusUpdate(true);
            unit.isLeavingTeleporter = true;
            Emulator.getThreading().run(new RoomUnitWalkToLocation(unit, tile, room, onSuccess, onSuccess));
        } else {
            WiredFreezeUtil.restoreWalkState(unit);
        }

        this.currentTeleport.setExtradata("1");
        this.room.updateItem(this.currentTeleport);

        Emulator.getThreading().run(new HabboItemNewState(this.currentTeleport, this.room, "0"), 1000);

        HabboItem teleportTile = this.room.getTopItemAt(unit.getX(), unit.getY());

        if (teleportTile != null && teleportTile instanceof InteractionTeleportTile && teleportTile != this.currentTeleport) {
            try {
                teleportTile.onWalkOn(unit, this.room, new Object[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
