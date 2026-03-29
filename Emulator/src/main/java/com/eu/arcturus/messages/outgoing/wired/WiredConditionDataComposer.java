package com.eu.arcturus.messages.outgoing.wired;

import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredCondition;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class WiredConditionDataComposer extends MessageComposer {
    private final InteractionWiredCondition condition;
    private final Room room;

    public WiredConditionDataComposer(InteractionWiredCondition condition, Room room) {
        this.condition = condition;
        this.room = room;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.WiredConditionDataComposer);
        this.condition.serializeWiredData(this.response, this.room);
        this.condition.needsUpdate(true);
        return this.response;
    }

    public InteractionWiredCondition getCondition() {
        return condition;
    }

    public Room getRoom() {
        return room;
    }
}
