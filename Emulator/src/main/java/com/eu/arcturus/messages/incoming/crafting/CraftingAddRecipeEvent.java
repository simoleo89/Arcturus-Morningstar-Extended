package com.eu.arcturus.messages.incoming.crafting;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.crafting.CraftingRecipe;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.AlertLimitedSoldOutComposer;
import com.eu.arcturus.messages.outgoing.crafting.CraftingRecipeComposer;

public class CraftingAddRecipeEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String recipeName = this.packet.readString();
        CraftingRecipe recipe = Emulator.getGameEnvironment().getCraftingManager().getRecipe(recipeName);

        if (recipe != null) {
            if (!recipe.canBeCrafted()) {
                this.client.sendResponse(new AlertLimitedSoldOutComposer());
                return;
            }

            this.client.sendResponse(new CraftingRecipeComposer(recipe));
        }
    }
}
