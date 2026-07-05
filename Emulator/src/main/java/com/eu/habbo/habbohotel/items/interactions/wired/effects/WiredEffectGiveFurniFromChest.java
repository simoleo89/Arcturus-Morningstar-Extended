package com.eu.habbo.habbohotel.items.interactions.wired.effects;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.items.Item;
import com.eu.habbo.habbohotel.items.interactions.InteractionWiredEffect;
import com.eu.habbo.habbohotel.items.interactions.wired.WiredSettings;
import com.eu.habbo.habbohotel.items.interactions.wired.chest.ChestWiredFurniUtil;
import com.eu.habbo.habbohotel.items.interactions.wired.chest.InteractionWiredChest;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomUnit;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.habbohotel.wired.WiredEffectType;
import com.eu.habbo.habbohotel.wired.core.WiredContext;
import com.eu.habbo.habbohotel.wired.core.WiredManager;
import com.eu.habbo.habbohotel.wired.core.WiredSourceUtil;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.incoming.wired.WiredSaveException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WiredEffectGiveFurniFromChest extends InteractionWiredEffect {
    public static final int CODE = 102;
    public static final WiredEffectType type = WiredEffectType.TOGGLE_STATE;

    private final Set<HabboItem> items = new LinkedHashSet<>();
    private int amount = 1;
    private int userSource = WiredSourceUtil.SOURCE_TRIGGER;

    public WiredEffectGiveFurniFromChest(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public WiredEffectGiveFurniFromChest(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public void execute(WiredContext ctx) {
        if (this.amount <= 0) {
            return;
        }

        Room room = ctx.room();
        InteractionWiredChest chest = this.resolveChest(room);
        if (chest == null) {
            return;
        }

        for (RoomUnit unit : WiredSourceUtil.resolveUsers(ctx, this.userSource)) {
            Habbo habbo = room.getHabbo(unit);
            if (habbo == null) {
                continue;
            }
            ChestWiredFurniUtil.giveFromChest(habbo, chest, this.amount);
        }
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
    public WiredEffectType getType() {
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
        message.appendInt(2);
        message.appendInt(this.amount);
        message.appendInt(this.userSource);
        message.appendInt(0);
        message.appendInt(CODE);
        message.appendInt(this.getDelay());
        message.appendInt(0);
    }

    @Override
    public boolean saveData(WiredSettings settings, GameClient gameClient) throws WiredSaveException {
        int[] params = settings.getIntParams();
        this.amount = (params.length > 0) ? Math.max(1, params[0]) : 1;
        this.userSource = (params.length > 1) ? params[1] : WiredSourceUtil.SOURCE_TRIGGER;
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
        this.setDelay(settings.getDelay());
        return true;
    }

    @Override
    public String getWiredData() {
        return WiredManager.getGson().toJson(new JsonData(this.amount, this.userSource, this.getDelay(),
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
        this.amount = Math.max(1, data.amount);
        this.userSource = data.userSource;
        this.setDelay(data.delay);
        this.loadSelectedItems(data.itemIds, room);
    }

    @Override
    public void onPickUp() {
        this.items.clear();
        this.amount = 1;
        this.userSource = WiredSourceUtil.SOURCE_TRIGGER;
        this.setDelay(0);
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
        int amount;
        int userSource;
        int delay;
        List<Integer> itemIds;

        JsonData(int amount, int userSource, int delay, List<Integer> itemIds) {
            this.amount = amount;
            this.userSource = userSource;
            this.delay = delay;
            this.itemIds = itemIds;
        }
    }
}
