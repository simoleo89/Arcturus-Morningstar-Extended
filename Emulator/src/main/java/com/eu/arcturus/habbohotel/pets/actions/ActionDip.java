package com.eu.arcturus.habbohotel.pets.actions;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.interactions.InteractionWater;
import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.pets.PetAction;
import com.eu.arcturus.habbohotel.pets.PetVocalsType;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import java.util.HashSet;

public class ActionDip extends PetAction {
    public ActionDip() {
        super(null, true);
    }

    @Override
    public boolean apply(Pet pet, Habbo habbo, String[] data) {
        HashSet<HabboItem> waterItems = pet.getRoom().getRoomSpecialTypes().getItemsOfType(InteractionWater.class);

        if (waterItems.isEmpty())
            return false;

        HabboItem waterPatch = (HabboItem) waterItems.toArray()[Emulator.getRandom().nextInt(waterItems.size())];

        pet.getRoomUnit().setGoalLocation(pet.getRoom().getLayout().getTile(waterPatch.getX(), waterPatch.getY()));
        
        pet.say(pet.getPetData().randomVocal(PetVocalsType.PLAYFUL));

        return true;
    }
}
