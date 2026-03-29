package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.Room;

public class PickallCommand extends Command {
    public PickallCommand() {
        super("cmd_pickall", Emulator.getTexts().getValue("commands.keys.cmd_pickall").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        Room room = gameClient.getHabbo().getHabboInfo().getCurrentRoom();

        if (room != null) {
            if (room.isOwner(gameClient.getHabbo())) {
                room.ejectAll();
                return true;
            }

            room.ejectUserFurni(gameClient.getHabbo().getHabboInfo().getId());
        }

        return true;
    }
}
