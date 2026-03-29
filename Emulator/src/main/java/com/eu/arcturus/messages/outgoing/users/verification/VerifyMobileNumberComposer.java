package com.eu.arcturus.messages.outgoing.users.verification;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class VerifyMobileNumberComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.VerifyMobileNumberComposer);
        return this.response;
    }
}