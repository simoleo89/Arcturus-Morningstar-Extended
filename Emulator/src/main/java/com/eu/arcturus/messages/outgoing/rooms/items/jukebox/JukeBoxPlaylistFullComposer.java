package com.eu.arcturus.messages.outgoing.rooms.items.jukebox;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class JukeBoxPlaylistFullComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.JukeBoxPlaylistFullComposer);
        return this.response;
    }
}
