package com.eu.arcturus.threading.runnables.teleport;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.items.interactions.InteractionTeleportTile;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.rooms.RoomUserRotation;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.habbohotel.wired.core.WiredFreezeUtil;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserStatusComposer;

public class TeleportActionOne implements Runnable {
    private final HabboItem currentTeleport;
    private final Room room;
    private final GameClient client;

    public TeleportActionOne(HabboItem currentTeleport, Room room, GameClient client) {
        this.currentTeleport = currentTeleport;
        this.client = client;
        this.room = room;
    }

    @Override
    public void run() {
        if (this.client.getHabbo().getHabboInfo().getCurrentRoom() != this.room) {
            this.client.getHabbo().getHabboInfo().setLoadingRoom(0);
            this.client.getHabbo().getRoomUnit().isTeleporting = false;
            WiredFreezeUtil.restoreWalkState(this.client.getHabbo().getRoomUnit());
            return;
        }

        int delay = 500;

        if (this.currentTeleport instanceof InteractionTeleportTile) {
            delay = 0;
        }

        if (this.client.getHabbo().getRoomUnit().getCurrentLocation() != this.room.getLayout().getTile(this.currentTeleport.getX(), this.currentTeleport.getY())) {
            this.client.getHabbo().getRoomUnit().setLocation(this.room.getLayout().getTile(this.currentTeleport.getX(), this.currentTeleport.getY()));
            this.client.getHabbo().getRoomUnit().setRotation(RoomUserRotation.values()[(this.currentTeleport.getRotation() + 4) % 8]);
            this.client.getHabbo().getRoomUnit().setStatus(RoomUnitStatus.MOVE, this.currentTeleport.getX() + "," + this.currentTeleport.getY() + "," + this.currentTeleport.getZ());
            this.room.scheduledComposers.add(new RoomUserStatusComposer(this.client.getHabbo().getRoomUnit()).compose());
            this.client.getHabbo().getRoomUnit().setLocation(this.room.getLayout().getTile(this.currentTeleport.getX(), this.currentTeleport.getY()));
        }

        Emulator.getThreading().run(new TeleportActionTwo(this.currentTeleport, this.room, this.client), delay);
    }
}
