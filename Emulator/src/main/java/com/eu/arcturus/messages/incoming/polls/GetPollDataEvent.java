package com.eu.arcturus.messages.incoming.polls;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.polls.Poll;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.polls.PollQuestionsComposer;

public class GetPollDataEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int pollId = this.packet.readInt();

        Poll poll = Emulator.getGameEnvironment().getPollManager().getPoll(pollId);

        if (poll != null) {
            this.client.sendResponse(new PollQuestionsComposer(poll));
        }
    }
}
