package com.eu.arcturus.messages.outgoing.inventory;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;


import java.util.NoSuchElementException;

public class InventoryPetsComposer extends MessageComposer {
    private final Habbo habbo;

    public InventoryPetsComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.InventoryPetsComposer);

        this.response.appendInt(1);
        this.response.appendInt(1);
        this.response.appendInt(this.habbo.getInventory().getPetsComponent().getPetsCount());

        for (Pet pet : this.habbo.getInventory().getPetsComponent().getPets().values()) {
            pet.serialize(this.response);
        }

        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
