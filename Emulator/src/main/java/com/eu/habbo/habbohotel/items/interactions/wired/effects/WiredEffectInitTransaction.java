package com.eu.habbo.habbohotel.items.interactions.wired.effects;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.items.Item;
import com.eu.habbo.habbohotel.items.interactions.InteractionWiredEffect;
import com.eu.habbo.habbohotel.items.interactions.InteractionWiredTrigger;
import com.eu.habbo.habbohotel.items.interactions.wired.WiredSettings;
import com.eu.habbo.habbohotel.items.interactions.wired.chest.InteractionWiredContract;
import com.eu.habbo.habbohotel.items.interactions.wired.chest.WiredTransactionExecutor;
import com.eu.habbo.habbohotel.items.interactions.wired.triggers.WiredTriggerTransactionComplete;
import com.eu.habbo.habbohotel.items.interactions.wired.triggers.WiredTriggerTransactionFail;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomUnit;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.habbohotel.wired.WiredEffectType;
import com.eu.habbo.habbohotel.wired.WiredHandler;
import com.eu.habbo.habbohotel.wired.core.WiredContext;
import com.eu.habbo.habbohotel.wired.core.WiredManager;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.incoming.wired.WiredSaveException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WiredEffectInitTransaction extends InteractionWiredEffect {
    public static final int CODE = 104;
    public static final WiredEffectType type = WiredEffectType.TOGGLE_STATE;

    private final Set<HabboItem> items = new LinkedHashSet<>();

    public WiredEffectInitTransaction(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public WiredEffectInitTransaction(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public void execute(WiredContext ctx) {
        Room room = ctx.room();
        RoomUnit roomUnit = ctx.actor().orElse(null);
        Habbo habbo = (roomUnit != null) ? room.getHabbo(roomUnit) : null;

        List<InteractionWiredContract> contracts = new ArrayList<>();
        for (HabboItem item : this.items) {
            HabboItem live = room.getHabboItem(item.getId());
            if (live instanceof InteractionWiredContract contract) {
                contracts.add(contract);
            }
        }

        boolean success = WiredTransactionExecutor.execute(habbo, room, contracts);
        this.fireBranchTriggers(room, roomUnit, success);
    }

    private void fireBranchTriggers(Room room, RoomUnit roomUnit, boolean success) {
        Class<? extends InteractionWiredTrigger> triggerClass = success
                ? WiredTriggerTransactionComplete.class
                : WiredTriggerTransactionFail.class;

        for (InteractionWiredTrigger trigger : room.getRoomSpecialTypes().getTriggers(this.getX(), this.getY())) {
            if (triggerClass.isInstance(trigger)) {
                WiredHandler.handle(trigger, roomUnit, room, new Object[0]);
            }
        }
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
        message.appendInt(0);
        message.appendInt(0);
        message.appendInt(CODE);
        message.appendInt(this.getDelay());
        message.appendInt(0);
    }

    @Override
    public boolean saveData(WiredSettings settings, GameClient gameClient) throws WiredSaveException {
        this.items.clear();
        Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(this.getRoomId());
        if (room == null) {
            return false;
        }

        for (int itemId : settings.getFurniIds()) {
            HabboItem item = room.getHabboItem(itemId);
            if (item instanceof InteractionWiredContract) {
                this.items.add(item);
            }
        }
        this.setDelay(settings.getDelay());
        return true;
    }

    @Override
    public String getWiredData() {
        return WiredManager.getGson().toJson(new JsonData(this.getDelay(),
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
        this.setDelay(data.delay);
        this.loadSelectedItems(data.itemIds, room);
    }

    @Override
    public void onPickUp() {
        this.items.clear();
        this.setDelay(0);
    }

    @Override
    public boolean requiresTriggeringUser() {
        return true;
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
            if (item instanceof InteractionWiredContract) {
                this.items.add(item);
            }
        }
    }

    static class JsonData {
        int delay;
        List<Integer> itemIds;

        JsonData(int delay, List<Integer> itemIds) {
            this.delay = delay;
            this.itemIds = itemIds;
        }
    }
}
