package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserIdleEvent extends UserEvent {
    public final IdleReason reason;
    public boolean idle;
    public UserIdleEvent(Habbo habbo, IdleReason reason, boolean idle) {
        super(habbo);

        this.reason = reason;
        this.idle = idle;
    }


    public enum IdleReason {
        ACTION,
        DANCE,
        TIMEOUT,
        WALKED,
        TALKED
    }
}