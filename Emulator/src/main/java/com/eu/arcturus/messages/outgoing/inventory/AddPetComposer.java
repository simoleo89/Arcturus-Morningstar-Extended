package com.eu.arcturus.messages.outgoing.inventory;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class AddPetComposer extends MessageComposer {
    private final Pet pet;

    public AddPetComposer(Pet pet) {
        this.pet = pet;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.AddPetComposer);
        this.pet.serialize(this.response);
        this.response.appendBoolean(false);
        return this.response;
    }

    public Pet getPet() {
        return pet;
    }
}
