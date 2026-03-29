package com.eu.arcturus.messages.outgoing.wired;

import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredTrigger;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class WiredTriggerDataComposer extends MessageComposer {
    private final InteractionWiredTrigger trigger;
    private final Room room;

    public WiredTriggerDataComposer(InteractionWiredTrigger trigger, Room room) {
        this.trigger = trigger;
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.WiredTriggerDataComposer);
        this.trigger.serializeWiredData(this.response, this.room);
        this.trigger.needsUpdate(true);
        return this.response;
    }

    public InteractionWiredTrigger getTrigger() {
        return trigger;
    }

    public Room getRoom() {
        return room;
    }
}
