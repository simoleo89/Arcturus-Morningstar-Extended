package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.messages.outgoing.users.UserDataComposer;

public class ChangeNameCommand extends Command {
    public ChangeNameCommand() {
        super("cmd_changename", Emulator.getTexts().getValue("commands.keys.cmd_changename").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        gameClient.getHabbo().getHabboStats().allowNameChange = !gameClient.getHabbo().getHabboStats().allowNameChange;
        gameClient.sendResponse(new UserDataComposer(gameClient.getHabbo()));
        return true;
    }
}
