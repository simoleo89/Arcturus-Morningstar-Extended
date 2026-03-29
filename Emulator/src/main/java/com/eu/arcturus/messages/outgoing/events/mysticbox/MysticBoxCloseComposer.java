package com.eu.arcturus.messages.outgoing.events.mysticbox;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class MysticBoxCloseComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.MysticBoxCloseComposer);
        return this.response;
    }
}
