package com.eu.arcturus.messages.outgoing.guides;

import com.eu.arcturus.habbohotel.guides.GuideChatMessage;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuideSessionMessageComposer extends MessageComposer {
    private final GuideChatMessage message;

    public GuideSessionMessageComposer(GuideChatMessage message) {
        this.message = message;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuideSessionMessageComposer);
        this.response.appendString(this.message.message); //Message
        this.response.appendInt(this.message.userId);   //Sender ID
        return this.response;
    }

    public GuideChatMessage getMessage() {
        return message;
    }
}
