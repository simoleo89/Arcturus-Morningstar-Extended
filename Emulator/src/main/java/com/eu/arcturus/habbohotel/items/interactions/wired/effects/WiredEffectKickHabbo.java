package com.eu.arcturus.habbohotel.items.interactions.wired.effects;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredEffect;
import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredTrigger;
import com.eu.arcturus.habbohotel.items.interactions.wired.WiredSettings;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessage;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.wired.WiredEffectType;
import com.eu.arcturus.habbohotel.wired.core.WiredManager;
import com.eu.arcturus.habbohotel.wired.core.WiredContext;
import com.eu.arcturus.habbohotel.wired.core.WiredSourceUtil;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.incoming.wired.WiredSaveException;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserWhisperComposer;
import com.eu.arcturus.threading.runnables.RoomUnitKick;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WiredEffectKickHabbo extends InteractionWiredEffect {
    private static final Logger LOGGER = LoggerFactory.getLogger(WiredEffectKickHabbo.class);
    public static final WiredEffectType type = WiredEffectType.KICK_USER;

    private String message = "";
    private int userSource = WiredSourceUtil.SOURCE_TRIGGER;

    public WiredEffectKickHabbo(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public WiredEffectKickHabbo(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public void execute(WiredContext ctx) {
        Room room = ctx.room();

        LOGGER.debug("[KickHabbo] targets.users().size={} usersModifiedBySelector={}",
                ctx.targets().users().size(), ctx.targets().isUsersModifiedBySelector());

        for (RoomUnit unit : WiredSourceUtil.resolveUsers(ctx, this.userSource)) {
            Habbo habbo = room.getHabbo(unit);
            LOGGER.debug("[KickHabbo] RoomUnit id={} type={} -> Habbo={}", unit.getId(), unit.getRoomUnitType(),
                    habbo != null ? habbo.getHabboInfo().getUsername() : "null");
            if (habbo == null) continue;

            if (habbo.hasPermission(Permission.ACC_UNKICKABLE)) {
                habbo.whisper(Emulator.getTexts().getValue("hotel.wired.kickexception.unkickable"));
                continue;
            }

            if (habbo.getHabboInfo().getId() == room.getOwnerId()) {
                habbo.whisper(Emulator.getTexts().getValue("hotel.wired.kickexception.owner"));
                continue;
            }

            room.giveEffect(habbo, 4, 2);

            if (!this.message.isEmpty())
                habbo.getClient().sendResponse(new RoomUserWhisperComposer(new RoomChatMessage(this.message, habbo, habbo, RoomChatMessageBubbles.ALERT)));

            Emulator.getThreading().run(new RoomUnitKick(habbo, room, true), 2000);
        }
    }

    @Deprecated
    @Override
    public boolean execute(RoomUnit roomUnit, Room room, Object[] stuff) {
        return false;
    }

    @Override
    public String getWiredData() {
        return WiredManager.getGson().toJson(new JsonData(this.message, this.getDelay(), this.userSource));
    }

    @Override
    public void loadWiredData(ResultSet set, Room room) throws SQLException {
        String wiredData = set.getString("wired_data");

        if(wiredData.startsWith("{")) {
            JsonData data = WiredManager.getGson().fromJson(wiredData, JsonData.class);
            this.setDelay(data.delay);
            this.message = data.message;
            this.userSource = data.userSource;
        }
        else {
            try {
                String[] data = set.getString("wired_data").split("\t");

                if (data.length >= 1) {
                    this.setDelay(Integer.parseInt(data[0]));

                    if (data.length >= 2) {
                        this.message = data[1];
                    }
                }
            } catch (Exception e) {
                this.message = "";
                this.setDelay(0);
            }

            this.needsUpdate(true);
            this.userSource = WiredSourceUtil.SOURCE_TRIGGER;
        }
    }

    @Override
    public void onPickUp() {
        this.message = "";
        this.userSource = WiredSourceUtil.SOURCE_TRIGGER;
        this.setDelay(0);
    }

    @Override
    public WiredEffectType getType() {
        return type;
    }

    @Override
    public void serializeWiredData(ServerMessage message, Room room) {
        message.appendBoolean(false);
        message.appendInt(5);
        message.appendInt(0);
        message.appendInt(this.getBaseItem().getSpriteId());
        message.appendInt(this.getId());
        message.appendString(this.message);
        message.appendInt(1);
        message.appendInt(this.userSource);
        message.appendInt(0);
        message.appendInt(this.getType().code);
        message.appendInt(this.getDelay());

        if (this.requiresTriggeringUser()) {
            List<Integer> invalidTriggers = new ArrayList<>();
            room.getRoomSpecialTypes().getTriggers(this.getX(), this.getY()).forEach(new Consumer<InteractionWiredTrigger>() {
                @Override
                public boolean execute(InteractionWiredTrigger object) {
                    if (!object.isTriggeredByRoomUnit()) {
                        invalidTriggers.add(object.getBaseItem().getSpriteId());
                    }
                    return true;
                }
            });
            message.appendInt(invalidTriggers.size());
            for (Integer i : invalidTriggers) {
                message.appendInt(i);
            }
        } else {
            message.appendInt(0);
        }
    }

    @Override
    public boolean saveData(WiredSettings settings, GameClient gameClient) throws WiredSaveException {
        String message = settings.getStringParam();
        int[] params = settings.getIntParams();
        this.userSource = (params.length > 0) ? params[0] : WiredSourceUtil.SOURCE_TRIGGER;
        int delay = settings.getDelay();

        if(delay > Emulator.getConfig().getInt("hotel.wired.max_delay", 20))
            throw new WiredSaveException("Delay too long");

        this.message = message.substring(0, Math.min(message.length(), Emulator.getConfig().getInt("hotel.wired.message.max_length", 100)));
        this.setDelay(delay);

        return true;
    }

    @Override
    public boolean requiresTriggeringUser() {
        return this.userSource == WiredSourceUtil.SOURCE_TRIGGER;
    }

    static class JsonData {
        String message;
        int delay;
        int userSource;

        public JsonData(String message, int delay, int userSource) {
            this.message = message;
            this.delay = delay;
            this.userSource = userSource;
        }
    }
}
