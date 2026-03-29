package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetTasks;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.users.Habbo;

public class ActionFree extends PetAction {
    public ActionFree() {
        super(PetTasks.FREE, false);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        pet.freeCommand();
        
        if (pet.getHappiness() > 50)
            pet.say(pet.getPetData().randomVocal(PetVocalsType.GENERIC_HAPPY));

        return true;
    }
}
