package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboBadge;
import com.eu.arcturus.habbohotel.users.HabboInfo;
import com.eu.arcturus.habbohotel.users.HabboManager;
import com.eu.arcturus.habbohotel.users.inventory.BadgesComponent;
import com.eu.arcturus.messages.outgoing.inventory.InventoryBadgesComposer;
import com.eu.arcturus.messages.outgoing.users.UserBadgesComposer;

public class TakeBadgeCommand extends Command {
    public TakeBadgeCommand() {
        super("cmd_take_badge", Emulator.getTexts().getValue("commands.keys.cmd_take_badge").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        if (params.length == 2) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_take_badge.forgot_badge"), RoomChatMessageBubbles.ALERT);
            return true;
        } else if (params.length == 1) {
            gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_take_badge.forgot_username"), RoomChatMessageBubbles.ALERT);
            return true;
        }

        if (params.length == 3) {
            String username = params[1];
            String badge = params[2];

            Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(username);

            if (habbo != null) {
                HabboBadge b = habbo.getInventory().getBadgesComponent().removeBadge(badge);

                if (b == null) {
                    gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.error.cmd_take_badge.no_badge").replace("%username%", username).replace("%badge%", badge), RoomChatMessageBubbles.ALERT);
                    return true;
                }

                habbo.getClient().sendResponse(new InventoryBadgesComposer(habbo));
                if (habbo.getHabboInfo().getCurrentRoom() != null) {
                    habbo.getHabboInfo().getCurrentRoom().sendComposer(new UserBadgesComposer(habbo.getInventory().getBadgesComponent().getWearingBadges(), habbo.getHabboInfo().getId()).compose());
                }
            }

            int userId = 0;

            if (habbo != null)
                userId = habbo.getHabboInfo().getId();
            else {
                HabboInfo habboInfo = HabboManager.getOfflineHabboInfo(username);
                if (habboInfo != null)
                    userId = habboInfo.getId();
            }

            if (userId > 0) {
                gameClient.getHabbo().whisper(Emulator.getTexts().getValue("commands.succes.cmd_take_badge"), RoomChatMessageBubbles.ALERT);

                BadgesComponent.deleteBadge(userId, badge);
            }
        }

        return true;
    }
}