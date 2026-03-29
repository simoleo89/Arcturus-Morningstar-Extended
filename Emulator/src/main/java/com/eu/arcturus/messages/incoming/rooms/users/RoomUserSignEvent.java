package com.eu.arcturus.messages.incoming.rooms.users;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.interactions.InteractionVoteCounter;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.habbohotel.wired.WiredUserActionType;
import com.eu.arcturus.habbohotel.wired.core.WiredManager;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.plugin.events.users.UserSignEvent;

public class RoomUserSignEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int signId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room == null)
            return;

        UserSignEvent event = new UserSignEvent(this.client.getHabbo(), signId);
        if (!Emulator.getPluginManager().fireEvent(event).isCancelled()) {
            this.client.getHabbo().getRoomUnit().setStatus(RoomUnitStatus.SIGN, event.sign + "");
            this.client.getHabbo().getHabboInfo().getCurrentRoom().unIdle(this.client.getHabbo());
            WiredManager.triggerUserPerformsAction(room, this.client.getHabbo().getRoomUnit(), WiredUserActionType.SIGN, event.sign);

            if(signId <= 10) {

                int userId = this.client.getHabbo().getHabboInfo().getId();
                for (HabboItem item : room.getFloorItems()) {
                    if (item instanceof InteractionVoteCounter) {
                        ((InteractionVoteCounter)item).vote(room, userId, signId);
                    }
                }
            }
        }
    }
}
