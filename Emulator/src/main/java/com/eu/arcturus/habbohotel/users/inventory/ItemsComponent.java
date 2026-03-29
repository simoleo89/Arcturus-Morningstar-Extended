package com.eu.arcturus.habbohotel.users.inventory;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboInventory;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.plugin.events.inventory.InventoryItemAddedEvent;
import com.eu.arcturus.plugin.events.inventory.InventoryItemRemovedEvent;
import com.eu.arcturus.plugin.events.inventory.InventoryItemsAddedEvent;
import java.util.Collections;

import java.util.Map;
import java.util.function.Consumer;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ItemsComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsComponent.class);

    private final Map<Integer, HabboItem> items = new java.util.concurrent.ConcurrentHashMap<>();

    private final HabboInventory inventory;

    public ItemsComponent(HabboInventory inventory, Habbo habbo) {
        this.inventory = inventory;
        this.items.putAll(loadItems(habbo));
    }

    public static HashMap<Integer, HabboItem> loadItems(Habbo habbo) {
        HashMap<Integer, HabboItem> itemsList = new HashMap<>();

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM items WHERE room_id = ? AND user_id = ?")) {
            statement.setInt(1, 0);
            statement.setInt(2, habbo.getHabboInfo().getId());
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    try {
                        HabboItem item = Emulator.getGameEnvironment().getItemManager().loadHabboItem(set);

                        if (item != null) {
                            itemsList.put(set.getInt("id"), item);
                        } else {
                            LOGGER.error("Failed to load HabboItem: {}", set.getInt("id"));
                        }
                    } catch (SQLException e) {
                        LOGGER.error("Caught SQL exception", e);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Caught SQL exception", e);
        }

        return itemsList;
    }

    public void addItem(HabboItem item) {
        if (item == null) {
            return;
        }

        InventoryItemAddedEvent event = new InventoryItemAddedEvent(this.inventory, item);
        if (Emulator.getPluginManager().fireEvent(event).isCancelled()) {
            return;
        }

        this.items.put(event.item.getId(), event.item);
    }

    public void addItems(HashSet<HabboItem> items) {
        InventoryItemsAddedEvent event = new InventoryItemsAddedEvent(this.inventory, items);
        if (Emulator.getPluginManager().fireEvent(event).isCancelled()) {
            return;
        }

        for (HabboItem item : event.items) {
            if (item == null) {
                continue;
            }

            this.items.put(item.getId(), item);
        }
    }

    public HabboItem getHabboItem(int itemId) {
        return this.items.get(Math.abs(itemId));
    }

    public HabboItem getAndRemoveHabboItem(final Item item) {
        final HabboItem[] habboItem = {null};
        synchronized (this.items) {
            this.items.values().forEach(new Consumer<HabboItem>() {
                @Override
                public void accept(HabboItem object) {
                    if (object.getBaseItem() == item) {
                        habboItem[0] = object;
                    }
                }
            });
        }
        this.removeHabboItem(habboItem[0]);
        return habboItem[0];
    }

    public void removeHabboItem(int itemId) {
        this.items.remove(itemId);
    }

    public void removeHabboItem(HabboItem item) {
        InventoryItemRemovedEvent event = new InventoryItemRemovedEvent(this.inventory, item);
        if (Emulator.getPluginManager().fireEvent(event).isCancelled()) {
            return;
        }

        this.items.remove(event.item.getId());
    }

    public Map<Integer, HabboItem> getItems() {
        return this.items;
    }

    public HashSet<HabboItem> getItemsAsValueCollection() {
        HashSet<HabboItem> items = new HashSet<>();
        items.addAll(this.items.values());

        return items;
    }

    public int itemCount() {
        return this.items.size();
    }

    public void dispose() {
        synchronized (this.items) {
            for (HabboItem item : this.items.values()) {
                if (item.needsUpdate())
                    Emulator.getThreading().run(item);
            }

            this.items.clear();
        }
    }
}
