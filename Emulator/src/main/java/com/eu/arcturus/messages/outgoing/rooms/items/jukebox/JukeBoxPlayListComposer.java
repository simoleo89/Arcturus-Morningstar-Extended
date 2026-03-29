package com.eu.arcturus.messages.outgoing.rooms.items.jukebox;

import com.eu.arcturus.habbohotel.items.interactions.InteractionMusicDisc;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.List;

public class JukeBoxPlayListComposer extends MessageComposer {
    private final List<InteractionMusicDisc> songs;
    private final int totalLength;

    public JukeBoxPlayListComposer(List<InteractionMusicDisc> songs, int totalLength) {
        this.songs = songs;
        this.totalLength = totalLength;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.JukeBoxPlayListComposer);
        this.response.appendInt(this.totalLength); // Total play length of all songs in seconds
        this.response.appendInt(this.songs.size());
        for (InteractionMusicDisc soundTrack : this.songs) {
            this.response.appendInt(soundTrack.getId());
            this.response.appendInt(soundTrack.getSongId());
        }
        return this.response;
    }

    public List<InteractionMusicDisc> getSongs() {
        return songs;
    }

    public int getTotalLength() {
        return totalLength;
    }
}
