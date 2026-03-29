package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetTasks;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.users.Habbo;

public class ActionStand extends PetAction {
    public ActionStand() {
        super(PetTasks.STAND, true);
        this.statusToRemove.add(RoomUnitStatus.MOVE);
        this.statusToRemove.add(RoomUnitStatus.LAY);
        this.statusToRemove.add(RoomUnitStatus.DEAD);
        this.statusToRemove.add(RoomUnitStatus.LAY);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        pet.clearPosture();

        if (pet.getHappiness() > 30)
            pet.say(pet.getPetData().randomVocal(PetVocalsType.PLAYFUL));
        else
            pet.say(pet.getPetData().randomVocal(PetVocalsType.GENERIC_NEUTRAL));

        return true;
    }
}
