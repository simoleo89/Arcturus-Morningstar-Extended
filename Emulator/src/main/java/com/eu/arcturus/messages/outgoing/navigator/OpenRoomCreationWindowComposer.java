package com.eu.arcturus.messages.outgoing.navigator;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class OpenRoomCreationWindowComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.OpenRoomCreationWindowComposer);
        return this.response;
    }
}
