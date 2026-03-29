package com.eu.arcturus.habbohotel.items.interactions.wired.conditions;

import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnit;
import com.eu.arcturus.habbohotel.wired.WiredConditionType;
import com.eu.arcturus.habbohotel.wired.core.WiredContext;
import com.eu.arcturus.habbohotel.wired.core.WiredSourceUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WiredConditionNotHabboWearsBadge extends WiredConditionHabboWearsBadge {
    public static final WiredConditionType type = WiredConditionType.NOT_ACTOR_WEARS_BADGE;

    public WiredConditionNotHabboWearsBadge(ResultSet set, Item baseItem) throws SQLException {
        super(set, baseItem);
    }

    public WiredConditionNotHabboWearsBadge(int id, int userId, Item item, String extradata, int limitedStack, int limitedSells) {
        super(id, userId, item, extradata, limitedStack, limitedSells);
    }

    @Override
    public boolean evaluate(WiredContext ctx) {
        Room room = ctx.room();
        List<RoomUnit> targets = WiredSourceUtil.resolveUsers(ctx, this.userSource);
        if (targets.isEmpty()) return true;

        if (this.getQuantifier() == QUANTIFIER_ALL) {
            return !this.matchesAllTargets(room, targets);
        }

        return !this.matchesAnyTarget(room, targets);
    }

    @Override
    public WiredConditionType getType() {
        return type;
    }
}
