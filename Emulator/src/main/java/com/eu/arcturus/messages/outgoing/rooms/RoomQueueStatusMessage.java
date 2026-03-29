package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RoomQueueStatusMessage extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RoomQueueStatusMessage);
        this.response.appendInt(1); //Count
        {
            this.response.appendString("TEST"); //Name
            this.response.appendInt(94); //Target

            this.response.appendInt(1); //Count
            this.response.appendString("d");
            this.response.appendInt(1);
        }
        return this.response;
    }
}