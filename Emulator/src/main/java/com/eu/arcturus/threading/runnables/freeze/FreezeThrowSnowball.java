package com.eu.arcturus.threading.runnables.freeze;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.games.freeze.FreezeGamePlayer;
import com.eu.arcturus.habbohotel.items.interactions.games.freeze.InteractionFreezeTile;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.threading.runnables.HabboItemNewState;

public class FreezeThrowSnowball implements Runnable {
    public final Habbo habbo;
    public final InteractionFreezeTile targetTile;
    public final Room room;
    public final int radius;

    public FreezeThrowSnowball(Habbo habbo, HabboItem targetTile, Room room) {
        this.habbo = habbo;
        this.targetTile = (InteractionFreezeTile) targetTile;
        this.room = room;
        this.radius = ((FreezeGamePlayer) habbo.getHabboInfo().getGamePlayer()).getExplosionBoost();
    }

    @Override
    public void run() {
        ((FreezeGamePlayer) this.habbo.getHabboInfo().getGamePlayer()).takeSnowball();
        this.targetTile.setExtradata((this.radius + 1) * 1000 + "");
        this.room.updateItem(this.targetTile);
        Emulator.getThreading().run(new FreezeHandleSnowballExplosion(this), 2000);
        Emulator.getThreading().run(new HabboItemNewState(this.targetTile, this.room, "11000"), 2000);
        Emulator.getThreading().run(new HabboItemNewState(this.targetTile, this.room, "0"), 3000);
    }
}
