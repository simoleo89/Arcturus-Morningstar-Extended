package com.eu.arcturus.messages.outgoing.guardians;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuardianVotingTimeEnded extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuardianVotingTimeEnded);
        return this.response;
    }
}
