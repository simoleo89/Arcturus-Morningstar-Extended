package com.eu.arcturus.messages.incoming.trading;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTrade;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.trading.TradeStartFailComposer;

public class TradeStartEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (Emulator.getIntUnixTimestamp() - this.client.getHabbo().getHabboStats().lastTradeTimestamp > 10) {
            this.client.getHabbo().getHabboStats().lastTradeTimestamp = Emulator.getIntUnixTimestamp();
            int userId = this.packet.readInt();

            Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();
            if (room != null) {
                if (userId >= 0 && userId != this.client.getHabbo().getRoomUnit().getId()) {
                    Habbo targetUser = room.getHabboByRoomUnitId(userId);

                    boolean tradeAnywhere = this.client.getHabbo().hasPermission(Permission.ACC_TRADE_ANYWHERE);

                    if (!RoomTrade.TRADING_ENABLED && !tradeAnywhere) {
                        this.client.sendResponse(new TradeStartFailComposer(TradeStartFailComposer.HOTEL_TRADING_NOT_ALLOWED));
                        return;
                    }

                    if ((room.getTradeMode() == 0 || (room.getTradeMode() == 1 && this.client.getHabbo().getHabboInfo().getId() != room.getOwnerId())) && !tradeAnywhere) {
                        this.client.sendResponse(new TradeStartFailComposer(TradeStartFailComposer.ROOM_TRADING_NOT_ALLOWED));
                        return;
                    }

                    if (targetUser == null) return;

                    if (targetUser.getHabboStats().userIgnored(this.client.getHabbo().getHabboInfo().getId())) return;

                    if (this.client.getHabbo().getRoomUnit().hasStatus(RoomUnitStatus.TRADING)) {
                        this.client.sendResponse(new TradeStartFailComposer(TradeStartFailComposer.YOU_ALREADY_TRADING));
                        return;
                    }

                    if (!this.client.getHabbo().getHabboStats().allowTrade()) {
                        this.client.sendResponse(new TradeStartFailComposer(TradeStartFailComposer.YOU_TRADING_OFF));
                        return;
                    }

                    if (targetUser.getRoomUnit().hasStatus(RoomUnitStatus.TRADING)) {
                        this.client.sendResponse(new TradeStartFailComposer(TradeStartFailComposer.TARGET_ALREADY_TRADING, targetUser.getHabboInfo().getUsername()));
                        return;
                    }

                    if (!targetUser.getHabboStats().allowTrade()) {
                        this.client.sendResponse(new TradeStartFailComposer(TradeStartFailComposer.TARGET_TRADING_NOT_ALLOWED, targetUser.getHabboInfo().getUsername()));
                        return;
                    }

                    room.startTrade(this.client.getHabbo(), targetUser);
                }
            }
        }
    }
}
