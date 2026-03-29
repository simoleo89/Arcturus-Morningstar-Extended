package com.eu.arcturus.habbohotel.modtool;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.events.support.SupportEvent;

public class ScripterEvent extends SupportEvent {
    public final Habbo habbo;
    public final String reason;

    public ScripterEvent(Habbo habbo, String reason) {
        super(null);

        this.habbo = habbo;
        this.reason = reason;
    }
}
