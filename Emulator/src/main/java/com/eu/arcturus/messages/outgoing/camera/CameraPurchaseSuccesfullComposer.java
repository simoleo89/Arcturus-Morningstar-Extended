package com.eu.arcturus.messages.outgoing.camera;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class CameraPurchaseSuccesfullComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.CameraPurchaseSuccesfullComposer);
        return this.response;
    }
}