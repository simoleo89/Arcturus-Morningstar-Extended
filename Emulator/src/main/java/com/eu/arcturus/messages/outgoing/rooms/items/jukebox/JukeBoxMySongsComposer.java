package com.eu.arcturus.messages.outgoing.rooms.items.jukebox;

import com.eu.arcturus.habbohotel.items.interactions.InteractionMusicDisc;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.List;

public class JukeBoxMySongsComposer extends MessageComposer {
    private final List<InteractionMusicDisc> items;

    public JukeBoxMySongsComposer(List<InteractionMusicDisc> items) {
        this.items = items;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.JukeBoxMySongsComposer);

        this.response.appendInt(this.items.size());

        for (HabboItem item : this.items) {
            this.response.appendInt(item.getId());
            this.response.appendInt(((InteractionMusicDisc) item).getSongId());
        }

        return this.response;
    }

    public List<InteractionMusicDisc> getItems() {
        return items;
    }
}
