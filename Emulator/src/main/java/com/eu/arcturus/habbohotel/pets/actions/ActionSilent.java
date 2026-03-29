package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.users.Habbo;

public class ActionSilent extends PetAction {
    public ActionSilent() {
        super(null, false);

        this.statusToRemove.add(RoomUnitStatus.SPEAK);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        pet.setMuted(true);
        pet.say(pet.getPetData().randomVocal(PetVocalsType.MUTED));

        return false;
    }
}
