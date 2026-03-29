package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.threading.runnables.PetClearPosture;

public class ActionWings extends PetAction {
    public ActionWings() {
        super(null, true);

        this.statusToSet.add(RoomUnitStatus.WINGS);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        Emulator.getThreading().run(new PetClearPosture(pet, RoomUnitStatus.WINGS, null, false), this.minimumActionDuration);

        if (pet.getHappiness() > 50)
            pet.say(pet.getPetData().randomVocal(PetVocalsType.PLAYFUL));

        return true;
    }
}
