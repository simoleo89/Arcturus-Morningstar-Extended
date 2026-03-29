package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;

public class UpdatePermissionsCommand extends Command {
    public UpdatePermissionsCommand() {
        super("cmd_update_permissions", Emulator.getTexts().getValue("commands.keys.cmd_update_permissions").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        Emulator.getGameEnvironment().getPermissionsManager().reload();

        gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_update_permissions"), RoomChatMessageBubbles.ALERT);

        return true;
    }
}
