package com.eu.arcturus.messages.outgoing.modtool;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class HelperRequestDisabledComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.HelperRequestDisabledComposer);
        this.response.appendString("");
        return this.response;
    }
}
