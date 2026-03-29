package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.Room;

public class HideWiredCommand extends Command {
    public HideWiredCommand() {
        super("cmd_hidewired", Emulator.getTexts().getValue("commands.keys.cmd_hidewired").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        Room room = gameClient.getHabbo().getHabboInfo().getCurrentRoom();

        if (room != null) {
            if (room.isOwner(gameClient.getHabbo())) {
                room.setHideWired(!room.isHideWired());
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_hidewired." + (room.isHideWired() ? "hidden" : "shown")));
            } else {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.errors.cmd_hidewired.permission"));
            }
        }

        return true;
    }
}
