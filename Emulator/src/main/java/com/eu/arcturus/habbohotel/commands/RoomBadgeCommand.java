package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboBadge;
import com.eu.arcturus.habbohotel.users.inventory.BadgesComponent;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.generic.alerts.BubbleAlertComposer;
import com.eu.arcturus.messages.outgoing.generic.alerts.BubbleAlertKeys;
import com.eu.arcturus.messages.outgoing.users.AddUserBadgeComposer;
import java.util.HashMap;

public class RoomBadgeCommand extends Command {
    public RoomBadgeCommand() {
        super("cmd_roombadge", Emulator.getTexts().getValue("commands.keys.cmd_roombadge").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (gameClient == null)
            return true;

        if (params.length == 2) {
            String badge;

            badge = params[1];

            if (!badge.isEmpty()) {
                HashMap<String, String> keys = new HashMap<>();
                keys.put("display", "BUBBLE");
                keys.put("image", "${image.library.url}album1584/" + badge + ".gif");
                keys.put("message", Emulator.getTexts().getValue("commands.generic.cmd_badge.received"));
                ServerMessage message = new BubbleAlertComposer(BubbleAlertKeys.RECEIVED_BADGE.key, keys).compose();

                for (Habbo habbo : gameClient.getHabbo().getRoomUnit().getRoom().getHabbos()) {
                    if (habbo.isOnline()) {
                        if (habbo.getInventory() != null && habbo.getInventory().getBadgesComponent() != null && !habbo.getInventory().getBadgesComponent().hasBadge(badge)) {
                            HabboBadge b = BadgesComponent.createBadge(badge, habbo);

                            habbo.getClient().sendResponse(new AddUserBadgeComposer(b));
                            habbo.getClient().sendResponse(message);
                        }
                    }
                }
            }
            return true;
        }

        gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_roombadge.no_badge"), RoomChatMessageBubbles.ALERT);
        return true;
    }
}
