package com.eu.arcturus.messages.outgoing.hotelview;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class HotelViewDataComposer extends MessageComposer {
    private final String data;
    private final String key;

    public HotelViewDataComposer(String data, String key) {
        this.data = data;
        this.key = key;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.HotelViewDataComposer);

        this.response.appendString(this.data);
        this.response.appendString(this.key);

        return this.response;
    }

    public String getData() {
        return data;
    }

    public String getKey() {
        return key;
    }
}
