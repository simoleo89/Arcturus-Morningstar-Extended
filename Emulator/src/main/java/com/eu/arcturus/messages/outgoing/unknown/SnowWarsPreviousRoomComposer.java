package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;

public class SnowWarsPreviousRoomComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(1381);
        this.response.appendInt(1); //room Id
        return this.response;
    }
}
