package com.eu.arcturus.plugin.events.emulator;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.plugin.events.users.UserEvent;

public class OutgoingPacketEvent extends UserEvent {
    private final MessageComposer composer;
    private final ServerMessage originalMessage;
    private ServerMessage customMessage;

    public OutgoingPacketEvent(Habbo habbo, MessageComposer composer, ServerMessage originalMessage) {
        super(habbo);
        this.composer = composer;
        this.originalMessage = originalMessage;
        this.customMessage = null;
    }

    public ServerMessage getOriginalMessage() {
        return originalMessage;
    }

    public MessageComposer getComposer() {
        return composer;
    }

    public void setCustomMessage(ServerMessage customMessage) {
        this.customMessage = customMessage;
    }

    public boolean hasCustomMessage() {
        return this.customMessage != null;
    }

    public ServerMessage getCustomMessage() {
        return this.customMessage;
    }
}