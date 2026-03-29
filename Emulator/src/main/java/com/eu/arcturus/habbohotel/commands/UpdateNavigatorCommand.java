package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;

public class UpdateNavigatorCommand extends Command {
    public UpdateNavigatorCommand() {
        super("cmd_update_navigator", Emulator.getTexts().getValue("commands.keys.cmd_update_navigator").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        Emulator.getGameEnvironment().getNavigatorManager().loadNavigator();
        Emulator.getGameEnvironment().getRoomManager().loadRoomModels();

        gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_update_navigator"), RoomChatMessageBubbles.ALERT);

        return true;
    }
}