package com.eu.arcturus.messages.incoming.rooms.items;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.interactions.InteractionExternalImage;
import com.eu.arcturus.habbohotel.items.interactions.InteractionPostIt;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.items.RemoveWallItemComposer;
import com.eu.arcturus.threading.runnables.QueryDeleteHabboItem;

public class PostItDeleteEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int itemId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room == null)
            return;

        HabboItem item = room.getHabboItem(itemId);

        if (item instanceof InteractionPostIt || item instanceof InteractionExternalImage) {
            if (item.getUserId() == this.client.getHabbo().getHabboInfo().getId() ||  this.client.getHabbo().hasPermission(Permission.ACC_ANYROOMOWNER)) {
                item.setRoomId(0);
                room.removeHabboItem(item);
                room.sendComposer(new RemoveWallItemComposer(item).compose());
                Emulator.getThreading().run(new QueryDeleteHabboItem(item.getId()));
            }
        }
    }
}
