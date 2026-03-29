package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.habbohotel.gameclients.GameClient;

public class HabnamCommand extends Command {
    public HabnamCommand() {
        super(null, new String[]{"habnam", "gangnam"});
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (gameClient.getHabbo().getHabboStats().hasActiveClub()) {
            if (gameClient.getHabbo().getHabboInfo().getCurrentRoom() != null) {
                gameClient.getHabbo().getHabboInfo().getCurrentRoom().giveEffect(gameClient.getHabbo(), 140, 30);
                return true;
            }
        }

        return false;
    }
}