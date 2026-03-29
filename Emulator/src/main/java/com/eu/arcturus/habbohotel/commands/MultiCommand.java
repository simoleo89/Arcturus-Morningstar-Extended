package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.messages.outgoing.rooms.items.PostItStickyPoleOpenComposer;

public class MultiCommand extends Command {
    public MultiCommand() {
        super("cmd_multi", Emulator.getTexts().getValue("commands.keys.cmd_multi").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        gameClient.sendResponse(new PostItStickyPoleOpenComposer(null));
        return true;
    }
}
