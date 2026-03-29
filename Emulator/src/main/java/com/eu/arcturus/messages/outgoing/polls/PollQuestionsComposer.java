package com.eu.arcturus.messages.outgoing.polls;

import com.eu.arcturus.habbohotel.polls.Poll;
import com.eu.arcturus.habbohotel.polls.PollQuestion;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class PollQuestionsComposer extends MessageComposer {
    private final Poll poll;

    public PollQuestionsComposer(Poll poll) {
        this.poll = poll;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.PollQuestionsComposer);

        this.response.appendInt(this.poll.id);
        this.response.appendString(this.poll.title);
        this.response.appendString(this.poll.thanksMessage);
        this.response.appendInt(this.poll.getQuestions().size());
        for (PollQuestion question : this.poll.getQuestions()) {
            question.serialize(this.response);
        }

        this.response.appendBoolean(true);
        return this.response;
    }

    public Poll getPoll() {
        return poll;
    }
}
