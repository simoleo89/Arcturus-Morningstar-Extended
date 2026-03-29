package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.modtool.WordFilterWord;
import com.eu.arcturus.habbohotel.users.Habbo;

public class UserTriggerWordFilterEvent extends UserEvent {

    public final WordFilterWord word;


    public UserTriggerWordFilterEvent(Habbo habbo, WordFilterWord word) {
        super(habbo);

        this.word = word;
    }
}
