package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;

public class UpdateWordFilterCommand extends Command {
    public UpdateWordFilterCommand() {
        super("cmd_update_wordfilter", Emulator.getTexts().getValue("commands.keys.update_wordfilter").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        Emulator.getGameEnvironment().getWordFilter().reload();

        gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_update_wordfilter"), RoomChatMessageBubbles.ALERT);

        return true;
    }
}
