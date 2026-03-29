package com.eu.habbo.messages.outgoing.rooms.items;

import com.eu.habbo.habbohotel.items.interactions.*;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

import java.util.Map;
import java.util.HashSet;


public class RoomFloorItemsComposer extends MessageComposer {
    private final Map<Integer, String> furniOwnerNames;
    private final HashSet<? extends HabboItem> items;

    public RoomFloorItemsComposer(Map<Integer, String> furniOwnerNames, HashSet<? extends HabboItem> items) {
        this.furniOwnerNames = furniOwnerNames;
        this.items = items;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomFloorItemsComposer);

        this.response.appendInt(this.furniOwnerNames.size());
        for (Map.Entry<Integer, String> entry : this.furniOwnerNames.entrySet()) {
            this.response.appendInt(entry.getKey());
            this.response.appendString(entry.getValue());
        }

        this.response.appendInt(this.items.size());

        for (HabboItem item : this.items) {
            item.serializeFloorData(this.response);

            int extraParam = switch (item) {
                case InteractionGift gift -> (gift.getColorId() * 1000) + gift.getRibbonId();
                case InteractionMusicDisc disc -> disc.getSongId();
                default -> 1;
            };
            this.response.appendInt(extraParam);

            item.serializeExtradata(this.response);
            this.response.appendInt(-1);

            int usability = switch (item) {
                case InteractionTeleport _,
                     InteractionSwitch _,
                     InteractionSwitchRemoteControl _,
                     InteractionVendingMachine _,
                     InteractionInformationTerminal _,
                     InteractionPostIt _,
                     InteractionPuzzleBox _ -> 2;
                default -> item.isUsable() ? 1 : 0;
            };
            this.response.appendInt(usability);

            this.response.appendInt(item.getUserId());
        }
        return this.response;
    }

    public Map<Integer, String> getFurniOwnerNames() {
        return furniOwnerNames;
    }

    public HashSet<? extends HabboItem> getItems() {
        return items;
    }
}
