package com.eu.habbo.habbohotel.items.interactions.wired.chest;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.items.Item;
import com.eu.habbo.habbohotel.items.interactions.wired.chest.ChestFurniStoredItem;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.outgoing.inventory.AddHabboItemComposer;
import com.eu.habbo.messages.outgoing.inventory.InventoryRefreshComposer;

import java.util.ArrayList;
import java.util.List;

/** Dispenses stored furni rows from a wired chest into a user's inventory. */
public final class ChestWiredFurniUtil {
    private ChestWiredFurniUtil() {
    }

    public static int giveFromChest(Habbo habbo, InteractionWiredChest chest, int amount) {
        if (habbo == null || chest == null || amount <= 0 || habbo.getClient() == null) {
            return 0;
        }

        ChestStorage contents = chest.getContents();
        List<ChestFurniStoredItem> removed = new ArrayList<>();
        var iterator = contents.furniItems().iterator();
        while (iterator.hasNext() && removed.size() < amount) {
            ChestFurniStoredItem stored = iterator.next();
            removed.add(stored);
            iterator.remove();
            contents.take(ChestStorage.KIND_FURNI, stored.baseItemId, 1);
        }

        if (removed.isEmpty()) {
            for (ChestStorage.Entry entry : contents.entries()) {
                if (entry.kind != ChestStorage.KIND_FURNI || entry.quantity <= 0) {
                    continue;
                }
                int takeCount = Math.min(amount, entry.quantity);
                removed.addAll(contents.removeFurniByBaseItemId(entry.type, takeCount));
                break;
            }
        }

        int given = 0;
        for (ChestFurniStoredItem stored : removed) {
            Item baseItem = Emulator.getGameEnvironment().getItemManager().getItem(stored.baseItemId);
            if (baseItem == null) {
                continue;
            }
            HabboItem created = Emulator.getGameEnvironment().getItemManager().createItem(
                    habbo.getHabboInfo().getId(), baseItem, stored.limitedStack, stored.limitedSells, stored.extradata);
            if (created == null) {
                continue;
            }
            habbo.getClient().sendResponse(new AddHabboItemComposer(created));
            habbo.getInventory().getItemsComponent().addItem(created);
            given++;
        }

        if (given > 0) {
            habbo.getClient().sendResponse(new InventoryRefreshComposer());
            chest.persistContents();
        }

        return given;
    }
}
