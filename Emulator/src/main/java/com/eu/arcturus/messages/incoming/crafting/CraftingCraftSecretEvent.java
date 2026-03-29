package com.eu.arcturus.messages.incoming.crafting;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.achievements.AchievementManager;
import com.eu.arcturus.habbohotel.crafting.CraftingAltar;
import com.eu.arcturus.habbohotel.crafting.CraftingRecipe;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.AlertLimitedSoldOutComposer;
import com.eu.arcturus.messages.outgoing.crafting.CraftingResultComposer;
import com.eu.arcturus.messages.outgoing.inventory.AddHabboItemComposer;
import com.eu.arcturus.messages.outgoing.inventory.InventoryRefreshComposer;
import com.eu.arcturus.messages.outgoing.inventory.RemoveHabboItemComposer;
import com.eu.arcturus.threading.runnables.QueryDeleteHabboItem;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;

public class CraftingCraftSecretEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int altarId = this.packet.readInt();
        int count = this.packet.readInt();

        HabboItem craftingAltar = this.client.getHabbo().getHabboInfo().getCurrentRoom().getHabboItem(altarId);

        if (craftingAltar != null) {
            CraftingAltar altar = Emulator.getGameEnvironment().getCraftingManager().getAltar(craftingAltar.getBaseItem());

            if (altar != null) {
                Set<HabboItem> habboItems = new HashSet<>();
                Map<Item, Integer> items = new HashMap<>();

                for (int i = 0; i < count; i++) {
                    HabboItem habboItem = this.client.getHabbo().getInventory().getItemsComponent().getHabboItem(this.packet.readInt());

                    if (habboItem == null) {
                        this.client.sendResponse(new CraftingResultComposer(null));
                        return;
                    }

                    habboItems.add(habboItem);

                    if (!items.containsKey(habboItem.getBaseItem())) {
                        items.put(habboItem.getBaseItem(), 0);
                    }

                    items.put(habboItem.getBaseItem(), items.get(habboItem.getBaseItem()) + 1);
                }

                CraftingRecipe recipe = altar.getRecipe(items);

                if (recipe != null) {
                    if (!recipe.canBeCrafted()) {
                        this.client.sendResponse(new AlertLimitedSoldOutComposer());
                        return;
                    }

                    HabboItem rewardItem = Emulator.getGameEnvironment().getItemManager().createItem(this.client.getHabbo().getHabboInfo().getId(), recipe.getReward(), 0, 0, "");

                    if (rewardItem != null) {
                        if (recipe.isLimited()) {
                            recipe.decrease();
                        }

                        if (!recipe.getAchievement().isEmpty()) {
                            AchievementManager.progressAchievement(this.client.getHabbo(), Emulator.getGameEnvironment().getAchievementManager().getAchievement(recipe.getAchievement()));
                        }

                        this.client.sendResponse(new CraftingResultComposer(recipe));
                        if (!this.client.getHabbo().getHabboStats().hasRecipe(recipe.getId())) {
                            this.client.getHabbo().getHabboStats().addRecipe(recipe.getId());
                            AchievementManager.progressAchievement(this.client.getHabbo(), Emulator.getGameEnvironment().getAchievementManager().getAchievement("AtcgSecret"));
                        }
                        this.client.getHabbo().getInventory().getItemsComponent().addItem(rewardItem);
                        this.client.sendResponse(new AddHabboItemComposer(rewardItem));
                        for (HabboItem item : habboItems) {
                            this.client.getHabbo().getInventory().getItemsComponent().removeHabboItem(item);
                            this.client.sendResponse(new RemoveHabboItemComposer(item.getGiftAdjustedId()));
                            Emulator.getThreading().run(new QueryDeleteHabboItem(item.getId()));
                        }
                        this.client.sendResponse(new InventoryRefreshComposer());

                        return;
                    }
                }
            }
        }

        this.client.sendResponse(new CraftingResultComposer(null));
    }
}
