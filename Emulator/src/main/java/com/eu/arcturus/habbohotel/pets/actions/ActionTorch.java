package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.threading.runnables.PetClearPosture;

public class ActionTorch extends PetAction {
    public ActionTorch() {
        super(null, true);

        this.minimumActionDuration = 1000;
        this.statusToSet.add(RoomUnitStatus.EAT);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        if (pet.getHappiness() < 30) {
            pet.say(pet.getPetData().randomVocal(PetVocalsType.DISOBEY));
            return false;
        }

        pet.say(pet.getPetData().randomVocal(PetVocalsType.PLAYFUL));
        Emulator.getThreading().run(new PetClearPosture(pet, RoomUnitStatus.EAT, null, false), this.minimumActionDuration);
        return true;
    }
}
