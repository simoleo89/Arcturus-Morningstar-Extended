package com.eu.arcturus.messages.outgoing.rooms.items.jukebox;

import com.eu.arcturus.habbohotel.items.SoundTrack;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.List;

public class JukeBoxTrackDataComposer extends MessageComposer {
    private final List<SoundTrack> tracks;

    public JukeBoxTrackDataComposer(List<SoundTrack> tracks) {
        this.tracks = tracks;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.JukeBoxTrackDataComposer);
        this.response.appendInt(this.tracks.size());

        for (SoundTrack track : this.tracks) {
            this.response.appendInt(track.getId());
            this.response.appendString(track.getCode());
            this.response.appendString(track.getName());
            this.response.appendString(track.getData());
            this.response.appendInt(track.getLength() * 1000);
            this.response.appendString(track.getAuthor());
        }

        return this.response;
    }

    public List<SoundTrack> getTracks() {
        return tracks;
    }
}
