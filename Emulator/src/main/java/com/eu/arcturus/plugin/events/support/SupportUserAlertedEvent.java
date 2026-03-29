package com.eu.arcturus.plugin.events.support;

import com.eu.arcturus.habbohotel.users.Habbo;

public class SupportUserAlertedEvent extends SupportEvent {

    public Habbo target;

    public String message;

    public SupportUserAlertedReason reason;

    public SupportUserAlertedEvent(Habbo moderator, Habbo target, String message, SupportUserAlertedReason reason) {
        super(moderator);

        this.message = message;
        this.target = target;
        this.reason = reason;
    }
}