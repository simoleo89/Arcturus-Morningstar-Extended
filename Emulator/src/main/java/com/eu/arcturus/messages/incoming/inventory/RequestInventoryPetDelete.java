package com.eu.arcturus.messages.incoming.inventory;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.inventory.InventoryPetsComposer;
import com.eu.arcturus.messages.outgoing.inventory.InventoryRefreshComposer;

public class RequestInventoryPetDelete extends MessageHandler {
    public int getRatelimit() {
        return 500;
    }

    public void handle() {
        final int petId = this.packet.readInt();
        final Habbo habbo = this.client.getHabbo();

        if (habbo == null)
            return;

        final Pet pet = habbo.getInventory().getPetsComponent().getPet(petId);

        if (pet == null)
            return;

        habbo.getInventory().getPetsComponent().removePet(pet);
        Emulator.getGameEnvironment().getPetManager().deletePet(pet);

        habbo.getClient().sendResponse(new InventoryRefreshComposer());
        habbo.getClient().sendResponse(new InventoryPetsComposer(habbo));
    }
}
