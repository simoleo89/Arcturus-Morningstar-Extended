package com.eu.arcturus.messages.outgoing.polls;

import com.eu.arcturus.habbohotel.polls.Poll;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class PollStartComposer extends MessageComposer {
    private final Poll poll;

    public PollStartComposer(Poll poll) {
        this.poll = poll;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.PollStartComposer);
        this.response.appendInt(this.poll.id);
        this.response.appendString(this.poll.title);
        this.response.appendString(this.poll.thanksMessage);
        this.response.appendString(this.poll.title);
        return this.response;
    }

    public Poll getPoll() {
        return poll;
    }
}
