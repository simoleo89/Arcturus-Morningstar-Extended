package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.habbohotel.users.Habbo;

import java.util.Map;

public class MassCreditsCommand extends Command {
    public MassCreditsCommand() {
        super("cmd_masscredits", Emulator.getTexts().getValue("commands.keys.cmd_masscredits").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (params.length == 2) {
            int amount;

            try {
                amount = Integer.parseInt(params[1]);
            } catch (Exception e) {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_masscredits.invalid_amount"), RoomChatMessageBubbles.ALERT);
                return true;
            }

            if (amount != 0) {
                for (Map.Entry<Integer, Habbo> set : Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos().entrySet()) {
                    Habbo habbo = set.getValue();

                    habbo.giveCredits(amount);

                    if (habbo.getHabboInfo().getCurrentRoom() != null)
                        habbo.whisper(Emulator.getTexts().getValue("commands.generic.cmd_credits.received").replace("%amount%", amount + ""), RoomChatMessageBubbles.ALERT);
                }
            }
            return true;
        }
        gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_masscredits.invalid_amount"), RoomChatMessageBubbles.ALERT);
        return true;
    }
}
