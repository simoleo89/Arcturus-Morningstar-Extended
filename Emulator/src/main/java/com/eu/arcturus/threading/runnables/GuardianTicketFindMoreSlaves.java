package com.eu.arcturus.threading.runnables;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guides.GuardianTicket;

public class GuardianTicketFindMoreSlaves implements Runnable {
    private final GuardianTicket ticket;

    public GuardianTicketFindMoreSlaves(GuardianTicket ticket) {
        this.ticket = ticket;
    }

    @Override
    public void run() {
        Emulator.getGameEnvironment().getGuideManager().findGuardians(this.ticket);
    }
}
