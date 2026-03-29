package com.eu.arcturus.plugin.events.pets;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.rooms.RoomChatMessage;

public class PetTalkEvent extends PetEvent {

    public RoomChatMessage message;

    public PetTalkEvent(Pet pet, RoomChatMessage message) {
        super(pet);

        this.message = message;
    }
}
