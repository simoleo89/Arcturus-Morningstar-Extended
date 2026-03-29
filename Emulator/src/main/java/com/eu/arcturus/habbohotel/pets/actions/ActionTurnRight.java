package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.rooms.RoomUserRotation;
import com.eu.arcturus.habbohotel.users.Habbo;

public class ActionTurnRight extends PetAction {
    public ActionTurnRight() {
        super(null, true);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        pet.getRoomUnit().setBodyRotation(RoomUserRotation.values()[(pet.getRoomUnit().getBodyRotation().getValue() + 1 > 7 ? 0 : pet.getRoomUnit().getBodyRotation().getValue() + 1)]);
        pet.say(pet.getPetData().randomVocal(PetVocalsType.GENERIC_NEUTRAL));
        return true;
    }
}
