package com.eu.arcturus.plugin.events.support;

import com.eu.arcturus.habbohotel.modtool.ModToolIssue;
import com.eu.arcturus.habbohotel.users.Habbo;

public class SupportTicketStatusChangedEvent extends SupportTicketEvent {

    public SupportTicketStatusChangedEvent(Habbo moderator, ModToolIssue ticket) {
        super(moderator, ticket);
    }
}