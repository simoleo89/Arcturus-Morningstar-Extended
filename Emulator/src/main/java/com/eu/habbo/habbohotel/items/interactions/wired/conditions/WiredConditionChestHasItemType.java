package com.eu.habbo.habbohotel.items.interactions.wired.conditions;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.items.Item;
import com.eu.habbo.habbohotel.items.interactions.InteractionWiredCondition;
import com.eu.habbo.habbohotel.items.interactions.wired.WiredSettings;
import com.eu.habbo.habbohotel.items.interactions.wired.chest.ChestStorage;
import com.eu.habbo.habbohotel.items.interactions.wired.chest.ChestWiredCompare;
import com.eu.habbo.habbohotel.items.interactions.wired.chest.InteractionWiredChest;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomUnit;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.habbohotel.wired.WiredConditionType;
import com.eu.habbo.habbohotel.wired.core.WiredContext;
import com.eu.habbo.habbohotel.wired.core.WiredManager;
import com.eu.habbo.messages.ServerMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WiredConditionChestHasItemType extends InteractionWiredCondition {
    public static final int CODE = 48;
    public static final WiredConditionType type = WiredConditionType.CHEST_HAS_ITEM_TYPE;

    private final Set<HabboItem> items = new LinkedHashSet<>();
    private int baseItemId = 0;
    private int amount = 1;
    private int comparison = ChestWiredCompare.GREATER_EQUAL;

    public WiredConditionChestHasItemType(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public WiredConditionChestHasItemType(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public boolean evaluate(WiredContext ctx) {
        InteractionWiredChest chest = this.resolveChest(ctx.room());
        if (chest == null || this.baseItemId <= 0) {
            return false;
        }

        int held = chest.getContents().count(ChestStorage.KIND_FURNI, this.baseItemId);
        return ChestWiredCompare.compare(held, this.amount, this.comparison);
    }

    private InteractionWiredChest resolveChest(Room room) {
        for (HabboItem item : this.items) {
            HabboItem live = room.getHabboItem(item.getId());
            if (live instanceof InteractionWiredChest chest) {
                return chest;
            }
        }
        return null;
    }

    @Deprecated
    @Override
    public boolean execute(RoomUnit roomUnit, Room room, Object[] stuff) {
        return false;
    }

    @Override
    public WiredConditionType getType() {
        return type;
    }

    @Override
    public void serializeWiredData(ServerMessage message, Room room) {
        this.refresh(room);
        message.appendBoolean(false);
        message.appendInt(WiredManager.MAXIMUM_FURNI_SELECTION);
        message.appendInt(this.items.size());
        for (HabboItem item : this.items) {
            message.appendInt(item.getId());
        }
        message.appendInt(this.getBaseItem().getSpriteId());
        message.appendInt(this.getId());
        message.appendString("");
        message.appendInt(3);
        message.appendInt(this.baseItemId);
        message.appendInt(this.amount);
        message.appendInt(this.comparison);
        message.appendInt(0);
        message.appendInt(CODE);
        message.appendInt(0);
        message.appendInt(0);
    }

    @Override
    public boolean saveData(WiredSettings settings) {
        int[] params = settings.getIntParams();
        this.baseItemId = (params.length > 0) ? Math.max(0, params[0]) : 0;
        this.amount = (params.length > 1) ? Math.max(1, params[1]) : 1;
        this.comparison = (params.length > 2) ? ChestWiredCompare.normalize(params[2]) : ChestWiredCompare.GREATER_EQUAL;
        this.items.clear();

        Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(this.getRoomId());
        if (room == null) {
            return false;
        }

        for (int itemId : settings.getFurniIds()) {
            HabboItem item = room.getHabboItem(itemId);
            if (item instanceof InteractionWiredChest) {
                this.items.add(item);
            }
        }
        return true;
    }

    @Override
    public String getWiredData() {
        return WiredManager.getGson().toJson(new JsonData(this.baseItemId, this.amount, this.comparison,
                this.items.stream().map(HabboItem::getId).collect(Collectors.toList())));
    }

    @Override
    public void loadWiredData(ResultSet set, Room room) throws SQLException {
        this.onPickUp();
        String wiredData = set.getString("wired_data");
        if (wiredData == null || !wiredData.startsWith("{")) {
            return;
        }
        JsonData data = WiredManager.getGson().fromJson(wiredData, JsonData.class);
        if (data == null) {
            return;
        }
        this.baseItemId = Math.max(0, data.baseItemId);
        this.amount = Math.max(1, data.amount);
        this.comparison = ChestWiredCompare.normalize(data.comparison);
        this.loadSelectedItems(data.itemIds, room);
    }

    @Override
    public void onPickUp() {
        this.items.clear();
        this.baseItemId = 0;
        this.amount = 1;
        this.comparison = ChestWiredCompare.GREATER_EQUAL;
    }

    private void refresh(Room room) {
        if (room == null) {
            return;
        }
        Set<HabboItem> stale = new HashSet<>();
        for (HabboItem item : this.items) {
            if (item == null || room.getHabboItem(item.getId()) == null) {
                stale.add(item);
            }
        }
        this.items.removeAll(stale);
    }

    private void loadSelectedItems(List<Integer> itemIds, Room room) {
        this.items.clear();
        if (itemIds == null || room == null) {
            return;
        }
        for (Integer itemId : itemIds) {
            HabboItem item = room.getHabboItem(itemId);
            if (item instanceof InteractionWiredChest) {
                this.items.add(item);
            }
        }
    }

    static class JsonData {
        int baseItemId;
        int amount;
        int comparison;
        List<Integer> itemIds;

        JsonData(int baseItemId, int amount, int comparison, List<Integer> itemIds) {
            this.baseItemId = baseItemId;
            this.amount = amount;
            this.comparison = comparison;
            this.itemIds = itemIds;
        }
    }
}
