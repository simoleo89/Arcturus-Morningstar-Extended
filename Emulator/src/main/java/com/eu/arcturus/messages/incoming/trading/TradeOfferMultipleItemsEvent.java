package com.eu.arcturus.messages.incoming.trading;

import com.eu.arcturus.habbohotel.rooms.RoomTrade;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;
import java.util.HashSet;

public class TradeOfferMultipleItemsEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (this.client.getHabbo().getHabboInfo().getCurrentRoom() == null)
            return;

        RoomTrade trade = this.client.getHabbo().getHabboInfo().getCurrentRoom().getActiveTradeForHabbo(this.client.getHabbo());

        if (trade == null)
            return;

        HashSet<HabboItem> items = new HashSet<>();

        int count = this.packet.readInt();
        for (int i = 0; i < count; i++) {
            HabboItem item = this.client.getHabbo().getInventory().getItemsComponent().getHabboItem(this.packet.readInt());
            if (item != null && item.getBaseItem().allowTrade()) {
                items.add(item);
            }
        }

        trade.offerMultipleItems(this.client.getHabbo(), items);
    }
}
