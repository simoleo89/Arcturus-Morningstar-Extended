package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetTasks;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.threading.runnables.PetClearPosture;

public class ActionFlatten extends PetAction {
    public ActionFlatten() {
        super(PetTasks.DOWN, true);
        this.minimumActionDuration = 3000;
        this.statusToSet.add(RoomUnitStatus.FLAT);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        pet.clearPosture();
        pet.getRoomUnit().setStatus(RoomUnitStatus.FLAT, pet.getRoomUnit().getCurrentLocation().getStackHeight() + "");

        Emulator.getThreading().run(new PetClearPosture(pet, RoomUnitStatus.FLAT, null, false), 3000);

        if (pet.getHappiness() > 50)
            pet.say(pet.getPetData().randomVocal(PetVocalsType.PLAYFUL));
        else
            pet.say(pet.getPetData().randomVocal(PetVocalsType.GENERIC_NEUTRAL));

        return true;
    }
}
