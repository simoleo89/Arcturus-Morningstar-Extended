package com.eu.arcturus.messages.incoming.catalog;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.SoundTrack;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.items.jukebox.JukeBoxTrackCodeComposer;

public class JukeBoxRequestTrackCodeEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String songName = this.packet.readString();

        final SoundTrack track = Emulator.getGameEnvironment().getItemManager().getSoundTrack(songName);

        if (track != null) {
            this.client.sendResponse(new JukeBoxTrackCodeComposer(track));
        }
    }
}
