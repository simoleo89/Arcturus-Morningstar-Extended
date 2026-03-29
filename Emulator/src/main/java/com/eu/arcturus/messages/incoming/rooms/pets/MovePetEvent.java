package com.eu.arcturus.messages.incoming.rooms.pets;

import com.eu.arcturus.habbohotel.pets.Pet;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.habbohotel.rooms.RoomUserRotation;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.users.RoomUserStatusComposer;

public class MovePetEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        Pet pet = this.client.getHabbo().getHabboInfo().getCurrentRoom().getPet(this.packet.readInt());

        if (pet != null) {
            Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();
            if (room != null && room.hasRights(this.client.getHabbo())) {
                if (pet.getRoomUnit() != null) {
                    int x = this.packet.readInt();
                    int y = this.packet.readInt();

                    RoomTile tile = room.getLayout().getTile((short) x, (short) y);

                    if (tile != null) {
                        pet.getRoomUnit().setLocation(tile);
                        pet.getRoomUnit().setPreviousLocation(tile);
                        pet.getRoomUnit().setZ(tile.z);
                        pet.getRoomUnit().setRotation(RoomUserRotation.fromValue(this.packet.readInt()));
                        pet.getRoomUnit().setPreviousLocationZ(pet.getRoomUnit().getZ());
                        room.sendComposer(new RoomUserStatusComposer(pet.getRoomUnit()).compose());
                        pet.needsUpdate = true;
                    }
                }
            }
        }
    }
}