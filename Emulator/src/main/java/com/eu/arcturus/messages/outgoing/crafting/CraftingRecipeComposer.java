package com.eu.arcturus.messages.outgoing.crafting;

import com.eu.arcturus.habbohotel.crafting.CraftingRecipe;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.Map;

public class CraftingRecipeComposer extends MessageComposer {
    private final CraftingRecipe recipe;

    public CraftingRecipeComposer(CraftingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.CraftingRecipeComposer);
        this.response.appendInt(this.recipe.getIngredients().size());

        for (Map.Entry<Item, Integer> ingredient : this.recipe.getIngredients().entrySet()) {
            this.response.appendInt(ingredient.getValue());
            this.response.appendString(ingredient.getKey().getName());
        }
        return this.response;
    }

    public CraftingRecipe getRecipe() {
        return recipe;
    }
}
