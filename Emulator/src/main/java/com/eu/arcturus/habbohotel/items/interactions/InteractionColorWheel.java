package com.eu.arcturus.habbohotel.items.interactions;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.rooms.items.WallItemUpdateComposer;
import com.eu.arcturus.threading.runnables.RandomDiceNumber;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InteractionColorWheel extends HabboItem {
    private Runnable rollTaks;

    public InteractionColorWheel(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public InteractionColorWheel(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public void serializeExtradata(ServerMessage serverMessage) {
        serverMessage.appendInt((this.isLimited() ? 256 : 0));
        serverMessage.appendString(this.getExtradata());

        super.serializeExtradata(serverMessage);
    }

    @Override
    public boolean canWalkOn(RoomUnit roomUnit, Room room, Object[] objects) {
        return true;
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public void onClick(GameClient client, Room room, Object[] objects) throws Exception {
        super.onClick(client, room, objects);

        if (!room.hasRights(client.getHabbo()))
            return;

        if (this.rollTaks == null && !this.getExtradata().equalsIgnoreCase("-1")) {
            this.setExtradata("-1");
            room.sendComposer(new WallItemUpdateComposer(this).compose());
            Emulator.getThreading().run(this);
            Emulator.getThreading().run(new RandomDiceNumber(this, room, this.getBaseItem().getStateCount()), 3000);
        }
    }

    @Override
    public void onWalk(RoomUnit roomUnit, Room room, Object[] objects) throws Exception {

    }

    @Override
    public void onWalkOn(RoomUnit client, Room room, Object[] objects) throws Exception {

    }

    @Override
    public void onWalkOff(RoomUnit roomUnit, Room room, Object[] objects) throws Exception {
        super.onWalkOff(roomUnit, room, objects);
    }

    public void clearRunnable() {
        this.rollTaks = null;
    }
}
