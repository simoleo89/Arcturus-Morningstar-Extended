package com.eu.arcturus.messages.incoming.rooms.items;

import com.eu.arcturus.habbohotel.items.interactions.InteractionPostIt;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.items.PostItDataComposer;

public class PostItRequestDataEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int itemId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room != null) {
            HabboItem item = room.getHabboItem(itemId);

            if (item instanceof InteractionPostIt) {
                this.client.sendResponse(new PostItDataComposer((InteractionPostIt) item));
            }
        }
    }
}
