package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetTasks;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.threading.runnables.PetFollowHabbo;

public class ActionFollowLeft extends PetAction {
    public ActionFollowLeft() {
        super(PetTasks.FOLLOW_LEFT, true);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        //Follow left.
        pet.clearPosture();

        Emulator.getThreading().run(new PetFollowHabbo(pet, habbo, +2));

        // Following owner is enjoyable
        pet.addHappiness(5);

        if (pet.getHappiness() > 75)
            pet.say(pet.getPetData().randomVocal(PetVocalsType.PLAYFUL));
        else
            pet.say(pet.getPetData().randomVocal(PetVocalsType.GENERIC_NEUTRAL));

        return true;
    }
}
