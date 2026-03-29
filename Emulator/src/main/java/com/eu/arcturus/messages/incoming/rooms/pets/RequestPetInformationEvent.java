package com.eu.arcturus.messages.incoming.rooms.pets;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.pets.PetInformationComposer;

public class RequestPetInformationEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int petId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room == null)
            return;

        Pet pet = room.getPet(petId);

        if (pet != null) {
            this.client.sendResponse(new PetInformationComposer(pet, room, this.client.getHabbo()));
        }
    }
}
