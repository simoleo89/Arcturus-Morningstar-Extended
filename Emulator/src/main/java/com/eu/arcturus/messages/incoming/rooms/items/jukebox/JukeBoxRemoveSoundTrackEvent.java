package com.eu.arcturus.messages.incoming.rooms.items.jukebox;

import com.eu.arcturus.habbohotel.items.interactions.InteractionMusicDisc;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class JukeBoxRemoveSoundTrackEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int index = this.packet.readInt();

        if (this.client.getHabbo().getHabboInfo().getCurrentRoom() == null) return;

        InteractionMusicDisc musicDisc = this.client.getHabbo().getHabboInfo().getCurrentRoom().getTraxManager().getSongs().get(index);

        if (musicDisc != null) {
            this.client.getHabbo().getHabboInfo().getCurrentRoom().getTraxManager().removeSong(musicDisc.getId());
        }
    }
}
