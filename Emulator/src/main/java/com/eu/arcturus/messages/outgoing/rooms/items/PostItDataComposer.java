package com.eu.arcturus.messages.outgoing.rooms.items;

import com.eu.arcturus.habbohotel.items.interactions.InteractionPostIt;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class PostItDataComposer extends MessageComposer {
    private final InteractionPostIt postIt;

    public PostItDataComposer(InteractionPostIt postIt) {
        this.postIt = postIt;
    }

    @Override
    protected ServerMessage composeInternal() {
        if (this.postIt.getExtradata().isEmpty() || this.postIt.getExtradata().length() < 6) {
            this.postIt.setExtradata("FFFF33");
        }

        this.response.init(Outgoing.PostItDataComposer);
        this.response.appendString(this.postIt.getId() + "");
        this.response.appendString(this.postIt.getExtradata());
        return this.response;
    }

    public InteractionPostIt getPostIt() {
        return postIt;
    }
}
