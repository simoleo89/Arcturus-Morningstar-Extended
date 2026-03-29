package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.messages.outgoing.modtool.ModToolIssueHandledComposer;

public class RoomAlertCommand extends Command {
    public RoomAlertCommand() {
        super("cmd_roomalert", Emulator.getTexts().getValue("commands.keys.cmd_roomalert").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        StringBuilder message = new StringBuilder();

        if (params.length >= 2) {
            for (int i = 1; i < params.length; i++) {
                message.append(params[i]).append(" ");
            }

            if (message.length() == 0) {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_roomalert.empty"), RoomChatMessageBubbles.ALERT);
                return true;
            }

            Room room = gameClient.getHabbo().getHabboInfo().getCurrentRoom();

            if (room != null) {
                room.sendComposer(new ModToolIssueHandledComposer(message.toString()).compose());
                return true;
            }
        }

        return false;
    }
}