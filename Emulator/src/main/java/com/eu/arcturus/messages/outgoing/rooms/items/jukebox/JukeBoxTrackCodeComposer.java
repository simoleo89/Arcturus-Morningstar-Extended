package com.eu.arcturus.messages.outgoing.rooms.items.jukebox;

import com.eu.arcturus.habbohotel.items.SoundTrack;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class JukeBoxTrackCodeComposer extends MessageComposer {
    private final SoundTrack track;

    public JukeBoxTrackCodeComposer(SoundTrack track) {
        this.track = track;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.JukeBoxTrackCodeComposer);
        this.response.appendString(this.track.getCode());
        this.response.appendInt(this.track.getId());
        return this.response;
    }

    public SoundTrack getTrack() {
        return track;
    }
}
