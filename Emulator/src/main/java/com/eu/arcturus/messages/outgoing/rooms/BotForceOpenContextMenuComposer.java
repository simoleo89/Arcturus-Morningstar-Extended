package com.eu.arcturus.messages.outgoing.rooms;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class BotForceOpenContextMenuComposer extends MessageComposer {
    private final int botId;

    public BotForceOpenContextMenuComposer(int botId) {
        this.botId = botId;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.BotForceOpenContextMenuComposer);
        this.response.appendInt(this.botId);
        return this.response;
    }

    public int getBotId() {
        return botId;
    }
}