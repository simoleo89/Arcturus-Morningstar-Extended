package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;

public class UpdateConfigCommand extends Command {
    public UpdateConfigCommand() {
        super("cmd_update_config", Emulator.getTexts().getValue("commands.keys.cmd_update_config").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        Emulator.getConfig().reload();

        gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_update_config"), RoomChatMessageBubbles.ALERT);

        return true;
    }
}
