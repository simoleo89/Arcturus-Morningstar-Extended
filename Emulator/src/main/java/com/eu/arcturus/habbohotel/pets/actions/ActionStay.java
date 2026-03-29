package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetTasks;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.rooms.RoomUnitStatus;
import com.eu.arcturus.habbohotel.users.Habbo;

public class ActionStay extends PetAction {
    public ActionStay() {
        super(PetTasks.STAY, true);

        this.statusToRemove.remove(RoomUnitStatus.MOVE);
        this.statusToRemove.remove(RoomUnitStatus.DEAD);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        pet.clearPosture();

        pet.getRoomUnit().setCanWalk(false);
        pet.setStayStartedAt(Emulator.getIntUnixTimestamp());
        
        // Staying still is boring
        pet.addHappiness(-5);
        
        pet.say(pet.getPetData().randomVocal(PetVocalsType.GENERIC_NEUTRAL));

        return true;
    }
}
