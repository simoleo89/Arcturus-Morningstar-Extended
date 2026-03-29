package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;

public class MutePetsCommand extends Command {
    public MutePetsCommand() {
        super(null, Emulator.getTexts().getValue("commands.keys.cmd_mute_pets").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        gameClient.getHabbo().getHabboStats().ignorePets = !gameClient.getHabbo().getHabboStats().ignorePets;
        gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_mute_pets." + (gameClient.getHabbo().getHabboStats().ignorePets ? "ignored" : "unignored")), RoomChatMessageBubbles.ALERT);
        return true;
    }
}