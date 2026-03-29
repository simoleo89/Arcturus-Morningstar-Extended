package com.eu.arcturus.plugin.events.pets;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.plugin.Event;

public abstract class PetEvent extends Event {

    public final Pet pet;


    public PetEvent(Pet pet) {
        this.pet = pet;
    }
}