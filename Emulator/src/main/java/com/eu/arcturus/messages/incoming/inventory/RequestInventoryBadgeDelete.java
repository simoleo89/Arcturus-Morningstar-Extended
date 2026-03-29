package com.eu.arcturus.messages.incoming.inventory;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboBadge;
import com.eu.arcturus.habbohotel.users.inventory.BadgesComponent;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.inventory.InventoryBadgesComposer;
import com.eu.arcturus.messages.outgoing.users.UserBadgesComposer;

public class RequestInventoryBadgeDelete extends MessageHandler {
    @Override
    public int getRatelimit() {
        return 500;
    }

    @Override
    public void handle() {
        final String badgeCode = this.packet.readString();
        final Habbo habbo = this.client.getHabbo();

        if (habbo == null || badgeCode == null || badgeCode.isEmpty())
            return;

        final HabboBadge badge = habbo.getInventory().getBadgesComponent().removeBadge(badgeCode);

        if (badge == null)
            return;

        BadgesComponent.deleteBadge(habbo.getHabboInfo().getId(), badge.getCode());

        this.client.sendResponse(new InventoryBadgesComposer(habbo));

        if (habbo.getHabboInfo().getCurrentRoom() != null)
            habbo.getHabboInfo().getCurrentRoom().sendComposer(new UserBadgesComposer(habbo.getInventory().getBadgesComponent().getWearingBadges(), habbo.getHabboInfo().getId()).compose());
        else
            this.client.sendResponse(new UserBadgesComposer(habbo.getInventory().getBadgesComponent().getWearingBadges(), habbo.getHabboInfo().getId()));
    }
}
