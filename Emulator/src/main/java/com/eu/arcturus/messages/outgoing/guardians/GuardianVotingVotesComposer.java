package com.eu.arcturus.messages.outgoing.guardians;

import com.eu.arcturus.habbohotel.guides.GuardianTicket;
import com.eu.arcturus.habbohotel.guides.GuardianVote;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.ArrayList;

public class GuardianVotingVotesComposer extends MessageComposer {
    private final GuardianTicket ticket;
    private final Habbo guardian;

    public GuardianVotingVotesComposer(GuardianTicket ticket, Habbo guardian) {
        this.ticket = ticket;
        this.guardian = guardian;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuardianVotingVotesComposer);

        ArrayList<GuardianVote> votes = this.ticket.getSortedVotes(this.guardian);

        this.response.appendInt(votes.size());

        for (GuardianVote vote : votes) {
            this.response.appendInt(vote.type.getType());
        }

        return this.response;
    }

    public GuardianTicket getTicket() {
        return ticket;
    }

    public Habbo getGuardian() {
        return guardian;
    }
}
