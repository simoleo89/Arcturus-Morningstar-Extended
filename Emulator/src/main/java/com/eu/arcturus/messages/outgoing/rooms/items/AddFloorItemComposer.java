package com.eu.arcturus.messages.outgoing.rooms.items;

import com.eu.arcturus.habbohotel.items.interactions.*;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class AddFloorItemComposer extends MessageComposer {
    private final HabboItem item;
    private final String itemOwnerName;

    public AddFloorItemComposer(HabboItem item, String itemOwnerName) {
        this.item = item;
        this.itemOwnerName = itemOwnerName == null ? "" : itemOwnerName;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.AddFloorItemComposer);
        this.item.serializeFloorData(this.response);

        int extraParam = switch (this.item) {
            case InteractionGift gift -> (gift.getColorId() * 1000) + gift.getRibbonId();
            case InteractionMusicDisc disc -> disc.getSongId();
            default -> 1;
        };
        this.response.appendInt(extraParam);

        this.item.serializeExtradata(this.response);
        this.response.appendInt(-1);

        int usability = switch (this.item) {
            case InteractionTeleport _,
                 InteractionSwitch _,
                 InteractionSwitchRemoteControl _,
                 InteractionVendingMachine _,
                 InteractionInformationTerminal _,
                 InteractionPostIt _,
                 InteractionPuzzleBox _ -> 2;
            default -> this.item.isUsable() ? 1 : 0;
        };
        this.response.appendInt(usability);

        this.response.appendInt(this.item.getUserId());
        this.response.appendString(this.itemOwnerName);
        return this.response;
    }

    public HabboItem getItem() {
        return item;
    }

    public String getItemOwnerName() {
        return itemOwnerName;
    }
}
