package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.habbohotel.items.interactions.pets.InteractionPetBreedingNest;
import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetTasks;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.outgoing.rooms.pets.breeding.PetBreedingStartFailedComposer;
import org.apache.commons.lang3.StringUtils;

public class ActionBreed extends PetAction {
    public ActionBreed() {
        super(PetTasks.BREED, true);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        InteractionPetBreedingNest nest = null;
        for (HabboItem item : pet.getRoom().getRoomSpecialTypes().getItemsOfType(InteractionPetBreedingNest.class)) {
            if (StringUtils.containsIgnoreCase(item.getBaseItem().getName(), pet.getPetData().getName())) {
                if (!((InteractionPetBreedingNest) item).boxFull()) {
                    nest = (InteractionPetBreedingNest) item;
                    break;
                }
            }
        }

        if (nest != null) {
            pet.getRoomUnit().setGoalLocation(pet.getRoom().getLayout().getTile(nest.getX(), nest.getY()));
            pet.say(pet.getPetData().randomVocal(PetVocalsType.GENERIC_HAPPY));

            return true;
        } else {
            habbo.getClient().sendResponse(new PetBreedingStartFailedComposer(PetBreedingStartFailedComposer.NO_NESTS));
        }

        return false;
    }
}
