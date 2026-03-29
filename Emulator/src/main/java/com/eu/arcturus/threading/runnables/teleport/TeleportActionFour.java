package com.eu.arcturus.threading.runnables.teleport;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.habbohotel.wired.core.WiredFreezeUtil;

class TeleportActionFour implements Runnable {
    private final HabboItem currentTeleport;
    private final Room room;
    private final GameClient client;

    public TeleportActionFour(HabboItem currentTeleport, Room room, GameClient client) {
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
            this.currentTeleport.setExtradata("0");
            this.room.updateItem(this.currentTeleport);
            return;
        }

        if(this.client.getHabbo().getRoomUnit() != null) {
            this.client.getHabbo().getRoomUnit().isLeavingTeleporter = true;
        }

        Emulator.getThreading().run(new TeleportActionFive(this.currentTeleport, this.room, this.client), 500);
    }
}
