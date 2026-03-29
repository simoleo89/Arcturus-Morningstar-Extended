package com.eu.arcturus.messages.outgoing.rooms.pets.breeding;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class PetBreedingFailedComposer extends MessageComposer {
    private final int anInt1;
    private final int anInt2;

    public PetBreedingFailedComposer(int anInt1, int anInt2) {
        this.anInt1 = anInt1;
        this.anInt2 = anInt2;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.PetBreedingFailedComposer);
        this.response.appendInt(this.anInt1);
        this.response.appendInt(this.anInt2);
        return this.response;
    }

    public int getAnInt1() {
        return anInt1;
    }

    public int getAnInt2() {
        return anInt2;
    }
}