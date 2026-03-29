package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;

public class UpdateTextsCommand extends Command {
    public UpdateTextsCommand() {
        super("cmd_update_texts", Emulator.getTexts().getValue("commands.keys.cmd_update_texts").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        try {
            Emulator.getTexts().reload();
            Emulator.getGameEnvironment().getCommandHandler().reloadCommands();
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_update_texts"), RoomChatMessageBubbles.ALERT);
        } catch (Exception e) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_update_texts.failed"), RoomChatMessageBubbles.ALERT);
        }

        return true;
    }
}
