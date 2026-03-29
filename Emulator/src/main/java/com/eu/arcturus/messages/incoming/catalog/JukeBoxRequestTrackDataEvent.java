package com.eu.arcturus.messages.incoming.catalog;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.SoundTrack;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.items.jukebox.JukeBoxTrackDataComposer;

import java.util.ArrayList;
import java.util.List;

public class JukeBoxRequestTrackDataEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int count = this.packet.readInt();

        List<SoundTrack> tracks = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            SoundTrack track = Emulator.getGameEnvironment().getItemManager().getSoundTrack(this.packet.readInt());

            if (track != null)
                tracks.add(track);
        }

        this.client.sendResponse(new JukeBoxTrackDataComposer(tracks));
    }
}
