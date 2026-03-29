package com.eu.arcturus.plugin.events.support;

import com.eu.arcturus.habbohotel.modtool.ModToolIssue;
import com.eu.arcturus.habbohotel.users.Habbo;

public class SupportTicketEvent extends SupportEvent {
    public final ModToolIssue ticket;


    public SupportTicketEvent(Habbo moderator, ModToolIssue ticket) {
        super(moderator);

        this.ticket = ticket;
    }
}
