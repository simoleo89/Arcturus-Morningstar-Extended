package com.eu.arcturus.messages.outgoing.users;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.catalog.ClothItem;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;
import java.util.function.IntConsumer;

import java.util.ArrayList;

public class UserClothesComposer extends MessageComposer {
    private final ArrayList<Integer> idList = new ArrayList<>();
    private final ArrayList<String> nameList = new ArrayList<>();

    public UserClothesComposer(Habbo habbo) {
        habbo.getInventory().getWardrobeComponent().getClothing().forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                ClothItem item = Emulator.getGameEnvironment().getCatalogManager().clothing.get(value);

                if (item != null) {
                    for (Integer j : item.setId) {
                        UserClothesComposer.this.idList.add(j);
                    }

                    UserClothesComposer.this.nameList.add(item.name);
                }
            }
        });
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UserClothesComposer);
        this.response.appendInt(this.idList.size());
        this.idList.forEach(this.response::appendInt);
        this.response.appendInt(this.nameList.size());
        this.nameList.forEach(this.response::appendString);
        return this.response;
    }

    public ArrayList<Integer> getIdList() {
        return idList;
    }

    public ArrayList<String> getNameList() {
        return nameList;
    }
}
