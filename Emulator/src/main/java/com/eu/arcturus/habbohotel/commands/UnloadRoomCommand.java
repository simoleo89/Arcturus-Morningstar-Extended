package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.Room;

public class UnloadRoomCommand extends Command {

    public UnloadRoomCommand() {
        super("cmd_unload", Emulator.getTexts().getValue("commands.keys.cmd_unload").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (gameClient.getHabbo().getHabboInfo().getCurrentRoom().getOwnerId() == gameClient.getHabbo().getHabboInfo().getId() || gameClient.getHabbo().getHabboInfo().getRank().getId() > 4) {
            Room room = gameClient.getHabbo().getHabboInfo().getCurrentRoom();

            room.dispose();
            return true;
        }
        return false;
    }
}
