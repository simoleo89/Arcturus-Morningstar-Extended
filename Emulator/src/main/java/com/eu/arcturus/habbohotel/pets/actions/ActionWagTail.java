package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetTasks;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.threading.runnables.PetClearPosture;

public class ActionWagTail extends PetAction {
    public ActionWagTail() {
        super(PetTasks.FREE, true);
        this.minimumActionDuration = 2000;
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        pet.clearPosture();
        pet.getRoomUnit().setStatus(RoomUnitStatus.WAG_TAIL, "");

        Emulator.getThreading().run(new PetClearPosture(pet, RoomUnitStatus.WAG_TAIL, null, false), 2000);

        if (pet.getHappiness() > 40)
            pet.say(pet.getPetData().randomVocal(PetVocalsType.GENERIC_HAPPY));
        else
            pet.say(pet.getPetData().randomVocal(PetVocalsType.GENERIC_NEUTRAL));

        return true;
    }
}
