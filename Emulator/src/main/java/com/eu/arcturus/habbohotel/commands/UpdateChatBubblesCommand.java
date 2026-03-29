package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;

public class UpdateChatBubblesCommand extends Command {

    public UpdateChatBubblesCommand() {
        super("cmd_update_chat_bubbles", Emulator.getTexts().getValue("commands.keys.cmd_update_chat_bubbles").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) {
        RoomChatMessageBubbles.removeDynamicBubbles();
        Emulator.getGameEnvironment().getRoomChatBubbleManager().reload();
        gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.success.cmd_update_chat_bubbles"), RoomChatMessageBubbles.ALERT);
        return true;
    }
}
