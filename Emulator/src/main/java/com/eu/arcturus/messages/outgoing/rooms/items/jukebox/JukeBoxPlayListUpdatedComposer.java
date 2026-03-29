package com.eu.arcturus.messages.outgoing.rooms.items.jukebox;

import com.eu.arcturus.habbohotel.items.SoundTrack;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;
import java.util.HashSet;

public class JukeBoxPlayListUpdatedComposer extends MessageComposer {
    private final HashSet<SoundTrack> tracks;

    public JukeBoxPlayListUpdatedComposer(HashSet<SoundTrack> tracks) {
        this.tracks = tracks;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.JukeBoxPlayListUpdatedComposer);

        int length = 0;

        for (SoundTrack track : this.tracks) {
            length += track.getLength();
        }

        this.response.appendInt(length * 1000);
        this.response.appendInt(this.tracks.size());

        for (SoundTrack track : this.tracks) {
            this.response.appendInt(track.getId());
            this.response.appendInt(track.getLength() * 1000);
            this.response.appendString(track.getCode());
            this.response.appendString(track.getAuthor());
        }

        return this.response;
    }

    public HashSet<SoundTrack> getTracks() {
        return tracks;
    }
}
