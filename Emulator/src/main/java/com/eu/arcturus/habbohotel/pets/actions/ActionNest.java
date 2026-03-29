package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.users.Habbo;

public class ActionNest extends PetAction {
    public ActionNest() {
        super(null, false);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        // Pet always obeys nest command - will go to nest or lay down if no nest available
        pet.findNest();

        if (pet.getEnergy() < 30)
            pet.say(pet.getPetData().randomVocal(PetVocalsType.TIRED));
        else
            pet.say(pet.getPetData().randomVocal(PetVocalsType.GENERIC_NEUTRAL));

        return true;
    }
}
