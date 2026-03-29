package com.eu.arcturus.messages.incoming.rooms.pets;

import com.eu.arcturus.habbohotel.items.interactions.pets.InteractionPetBreedingNest;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class StopBreedingEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int itemId = this.packet.readInt();

        HabboItem item = this.client.getHabbo().getHabboInfo().getCurrentRoom().getHabboItem(itemId);

        if (item instanceof InteractionPetBreedingNest) {
            ((InteractionPetBreedingNest) item).stopBreeding(this.client.getHabbo());
        }
    }
}