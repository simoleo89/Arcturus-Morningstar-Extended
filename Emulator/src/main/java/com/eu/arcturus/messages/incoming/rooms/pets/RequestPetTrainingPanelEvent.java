package com.eu.arcturus.messages.incoming.rooms.pets;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.pets.PetTrainingPanelComposer;

public class RequestPetTrainingPanelEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int petId = this.packet.readInt();

        if (this.client.getHabbo().getHabboInfo().getCurrentRoom() == null)
            return;

        Pet pet = this.client.getHabbo().getHabboInfo().getCurrentRoom().getPet(petId);

        if (pet != null)
            this.client.sendResponse(new PetTrainingPanelComposer(pet));
    }
}
