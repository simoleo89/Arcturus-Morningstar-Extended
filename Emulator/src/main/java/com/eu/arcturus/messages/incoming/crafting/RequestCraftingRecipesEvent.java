package com.eu.arcturus.messages.incoming.crafting;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.crafting.CraftingAltar;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.crafting.CraftableProductsComposer;

public class RequestCraftingRecipesEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int itemId = this.packet.readInt();
        HabboItem item = this.client.getHabbo().getHabboInfo().getCurrentRoom().getHabboItem(itemId);

        if (item != null) {
            CraftingAltar altar = Emulator.getGameEnvironment().getCraftingManager().getAltar(item.getBaseItem());

            if (altar != null) {
                this.client.sendResponse(new CraftableProductsComposer(altar.getRecipesForHabbo(this.client.getHabbo()), altar.getIngredients()));
            }
        }
    }
}
