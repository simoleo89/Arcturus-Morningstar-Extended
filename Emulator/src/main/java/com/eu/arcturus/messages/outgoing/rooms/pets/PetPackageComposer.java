package com.eu.arcturus.messages.outgoing.rooms.pets;

import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class PetPackageComposer extends MessageComposer {
    private final HabboItem item;

    public PetPackageComposer(HabboItem item) {
        this.item = item;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.LeprechaunStarterBundleComposer);
        this.response.appendInt(this.item.getId());
        return this.response;
    }

    public HabboItem getItem() {
        return item;
    }
}