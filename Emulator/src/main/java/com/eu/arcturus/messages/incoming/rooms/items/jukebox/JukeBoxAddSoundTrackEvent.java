package com.eu.arcturus.messages.incoming.rooms.items.jukebox;

import com.eu.arcturus.habbohotel.items.interactions.InteractionMusicDisc;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class JukeBoxAddSoundTrackEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (!this.client.getHabbo().getHabboInfo().getCurrentRoom().hasRights(this.client.getHabbo())) return;

        int itemId = this.packet.readInt();
        this.packet.readInt(); // slotId

        Habbo habbo = this.client.getHabbo();

        if (habbo != null) {
            HabboItem item = habbo.getInventory().getItemsComponent().getHabboItem(itemId);

            if (item instanceof InteractionMusicDisc && item.getRoomId() == 0) {
                this.client.getHabbo().getHabboInfo().getCurrentRoom().getTraxManager().addSong((InteractionMusicDisc) item, habbo);
            }
        }
    }
}