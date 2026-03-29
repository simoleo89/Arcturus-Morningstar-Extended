package com.eu.arcturus.habbohotel.games.tag;

import com.eu.arcturus.habbohotel.games.GameTeam;
import com.eu.arcturus.habbohotel.items.interactions.games.tag.InteractionTagPole;
import com.eu.arcturus.habbohotel.items.interactions.games.tag.bunnyrun.InteractionBunnyrunPole;
import com.eu.arcturus.habbohotel.rooms.Room;

public class BunnyrunGame extends TagGame {
    public BunnyrunGame(Room room) {
        super(GameTeam.class, TagGamePlayer.class, room);
    }

    @Override
    public Class<? extends InteractionTagPole> getTagPole() {
        return InteractionBunnyrunPole.class;
    }

    @Override
    public int getMaleEffect() {
        return 0;
    }

    @Override
    public int getMaleTaggerEffect() {
        return 68;
    }

    @Override
    public int getFemaleEffect() {
        return 0;
    }

    @Override
    public int getFemaleTaggerEffect() {
        return 68;
    }
}