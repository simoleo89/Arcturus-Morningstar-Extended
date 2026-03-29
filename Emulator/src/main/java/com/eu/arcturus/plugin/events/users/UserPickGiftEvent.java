package com.eu.arcturus.plugin.events.users;

import com.eu.arcturus.habbohotel.users.Habbo;

public class UserPickGiftEvent extends UserEvent {
    public final int keyA;
    public final int keyB;
    public final int index;


    public UserPickGiftEvent(Habbo habbo, int keyA, int keyB, int index) {
        super(habbo);
        this.keyA = keyA;
        this.keyB = keyB;
        this.index = index;
    }
}
