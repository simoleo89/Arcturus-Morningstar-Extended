package com.eu.arcturus.messages.outgoing.habboway;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class HabboWayQuizComposer2 extends MessageComposer {
    public final String name;
    public final int[] items;

    public HabboWayQuizComposer2(String name, int[] items) {
        this.name = name;
        this.items = items;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.HabboWayQuizComposer2);
        this.response.appendString(this.name);
        this.response.appendInt(this.items.length);
        for (int item : this.items) {
            this.response.appendInt(item);
        }
        return this.response;
    }

    public String getName() {
        return name;
    }

    public int[] getItems() {
        return items;
    }
}