package com.eu.arcturus.plugin.events.sanctions;

import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.events.support.SupportEvent;

public class SanctionEvent extends SupportEvent {
    public Habbo target;

    public int sanctionLevel;

    public SanctionEvent(Habbo moderator, Habbo target, int sanctionLevel) {
        super(moderator);

        this.target = target;
        this.sanctionLevel = sanctionLevel;
    }
}
