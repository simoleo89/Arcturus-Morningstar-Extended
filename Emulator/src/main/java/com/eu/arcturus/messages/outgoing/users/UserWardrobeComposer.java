package com.eu.arcturus.messages.outgoing.users;

import com.eu.arcturus.habbohotel.users.inventory.WardrobeComponent;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class UserWardrobeComposer extends MessageComposer {
    private final WardrobeComponent wardrobeComponent;

    public UserWardrobeComposer(WardrobeComponent wardrobeComponent) {
        this.wardrobeComponent = wardrobeComponent;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UserWardrobeComposer);
        this.response.appendInt(1);
        this.response.appendInt(this.wardrobeComponent.getLooks().size());
        for (WardrobeComponent.WardrobeItem wardrobeItem : this.wardrobeComponent.getLooks().values()) {
            this.response.appendInt(wardrobeItem.getSlotId());
            this.response.appendString(wardrobeItem.getLook());
            this.response.appendString(wardrobeItem.getGender().name().toUpperCase());
        }
        return this.response;
    }

    public WardrobeComponent getWardrobeComponent() {
        return wardrobeComponent;
    }
}
