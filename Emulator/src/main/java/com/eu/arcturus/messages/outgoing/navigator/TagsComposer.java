package com.eu.arcturus.messages.outgoing.navigator;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.Set;

public class TagsComposer extends MessageComposer {
    private final Set<String> tags;

    public TagsComposer(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.TagsComposer);
        this.response.appendInt(this.tags.size());

        int i = 1;
        for (String s : this.tags) {
            this.response.appendString(s);
            this.response.appendInt(i);
            i++;
        }

        return this.response;
    }

    public Set<String> getTags() {
        return tags;
    }
}
