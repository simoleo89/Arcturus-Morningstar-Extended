package com.eu.arcturus.messages.outgoing.inventory;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RemovePetComposer extends MessageComposer {
    private final int petId;

    public RemovePetComposer(Pet pet) {
        this.petId = pet.getId();
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RemovePetComposer);
        this.response.appendInt(this.petId);
        return this.response;
    }

    public int getPetId() {
        return petId;
    }
}
