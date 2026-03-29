package com.eu.arcturus.threading.runnables;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guides.GuardianTicket;
import com.eu.arcturus.habbohotel.guides.GuardianVote;
import com.eu.arcturus.habbohotel.guides.GuardianVoteType;
import com.eu.arcturus.habbohotel.users.Habbo;

public class GuardianNotAccepted implements Runnable {
    private final GuardianTicket ticket;
    private final Habbo habbo;

    public GuardianNotAccepted(GuardianTicket ticket, Habbo habbo) {
        this.ticket = ticket;
        this.habbo = habbo;
    }

    @Override
    public void run() {
        GuardianVote vote = this.ticket.getVoteForGuardian(this.habbo);

        if (vote != null) {
            if (vote.type == GuardianVoteType.SEARCHING) {
                Emulator.getGameEnvironment().getGuideManager().acceptTicket(this.habbo, false);
            }
        }
    }
}
