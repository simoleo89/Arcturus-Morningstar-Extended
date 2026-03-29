package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.messenger.Message;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.messages.outgoing.friends.FriendChatMessageComposer;

public class StaffAlertCommand extends Command {
    public StaffAlertCommand() {
        super("cmd_staffalert", Emulator.getTexts().getValue("commands.keys.cmd_staffalert").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (params.length > 1) {
            StringBuilder message = new StringBuilder();
            for (int i = 1; i < params.length; i++) {
                message.append(params[i]).append(" ");
            }

            Emulator.getGameEnvironment().getHabboManager().staffAlert(message + "\r\n-" + gameClient.getHabbo().getHabboInfo().getUsername());
            Emulator.getGameServer().getGameClientManager().sendBroadcastResponse(new FriendChatMessageComposer(new Message(gameClient.getHabbo().getHabboInfo().getId(), -1, message.toString())).compose(), "acc_staff_chat", gameClient);
        } else {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_staffalert.forgot_message"), RoomChatMessageBubbles.ALERT);
        }

        return true;
    }
}
