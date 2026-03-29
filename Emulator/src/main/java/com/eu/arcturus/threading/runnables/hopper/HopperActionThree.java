package com.eu.arcturus.threading.runnables.hopper;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.achievements.AchievementManager;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.items.interactions.InteractionCostumeHopper;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.rooms.RoomUserRotation;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserStatusComposer;
import com.eu.arcturus.threading.runnables.HabboItemNewState;

class HopperActionThree implements Runnable {
    private final HabboItem teleportOne;
    private final Room room;
    private final GameClient client;
    private final int targetRoomId;
    private final int targetItemId;

    public HopperActionThree(HabboItem teleportOne, Room room, GameClient client, int targetRoomId, int targetItemId) {
        this.teleportOne = teleportOne;
        this.room = room;
        this.client = client;
        this.targetRoomId = targetRoomId;
        this.targetItemId = targetItemId;
    }

    @Override
    public void run() {
        HabboItem targetTeleport;
        Room targetRoom = this.room;

        if (this.teleportOne.getRoomId() != this.targetRoomId) {
            Emulator.getGameEnvironment().getRoomManager().leaveRoom(this.client.getHabbo(), this.room, false);
            targetRoom = Emulator.getGameEnvironment().getRoomManager().loadRoom(this.targetRoomId);
            Emulator.getGameEnvironment().getRoomManager().enterRoom(this.client.getHabbo(), targetRoom.getId(), "", false);
        }

        targetTeleport = targetRoom.getHabboItem(this.targetItemId);

        if (targetTeleport == null) {
            this.client.getHabbo().getRoomUnit().removeStatus(RoomUnitStatus.MOVE);
            this.client.getHabbo().getRoomUnit().isTeleporting = false;
            this.client.getHabbo().getRoomUnit().setCanWalk(true);
            return;
        }

        targetTeleport.setExtradata("2");
        targetRoom.updateItem(targetTeleport);
        this.client.getHabbo().getRoomUnit().setLocation(this.room.getLayout().getTile(targetTeleport.getX(), targetTeleport.getY()));
        this.client.getHabbo().getRoomUnit().setPreviousLocationZ(targetTeleport.getZ());
        this.client.getHabbo().getRoomUnit().setZ(targetTeleport.getZ());
        this.client.getHabbo().getRoomUnit().setRotation(RoomUserRotation.values()[targetTeleport.getRotation() % 8]);
        this.client.getHabbo().getRoomUnit().removeStatus(RoomUnitStatus.MOVE);
        targetRoom.sendComposer(new RoomUserStatusComposer(this.client.getHabbo().getRoomUnit()).compose());

        Emulator.getThreading().run(new HabboItemNewState(this.teleportOne, this.room, "0"), 500);
        Emulator.getThreading().run(new HopperActionFour(targetTeleport, targetRoom, this.client), 500);

        if (targetTeleport instanceof InteractionCostumeHopper) {
            AchievementManager.progressAchievement(this.client.getHabbo(), Emulator.getGameEnvironment().getAchievementManager().getAchievement("CostumeHopper"));
        }
    }
}
