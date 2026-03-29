package com.eu.arcturus.messages.outgoing.catalog;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class RedeemVoucherOKComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.RedeemVoucherOKComposer);
        this.response.appendString("");
        this.response.appendString("");
        return this.response;
    }
}
