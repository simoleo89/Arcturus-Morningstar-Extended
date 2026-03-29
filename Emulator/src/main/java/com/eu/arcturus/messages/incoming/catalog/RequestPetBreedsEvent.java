package com.eu.arcturus.messages.incoming.catalog;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.catalog.PetBreedsComposer;

public class RequestPetBreedsEvent extends MessageHandler {
    @Override
    public int getRatelimit() {
        return 500;
    }

    @Override
    public void handle() throws Exception {
        final String petName = this.packet.readString();
        this.client.sendResponse(new PetBreedsComposer(petName, Emulator.getGameEnvironment().getPetManager().getBreeds(petName)));
    }
}