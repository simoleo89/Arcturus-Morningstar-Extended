package com.eu.arcturus.messages.outgoing.generic.alerts;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class HotelWillCloseInMinutesComposer extends MessageComposer {
    private final int minutes;

    public HotelWillCloseInMinutesComposer(int minutes) {
        this.minutes = minutes;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.HotelWillCloseInMinutesComposer);
        this.response.appendInt(this.minutes);
        return this.response;
    }

    public int getMinutes() {
        return minutes;
    }
}
