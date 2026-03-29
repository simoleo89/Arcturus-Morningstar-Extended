package com.eu.arcturus.plugin.events.bots;

import com.eu.arcturus.habbohotel.bots.Bot;
import com.eu.arcturus.habbohotel.users.HabboGender;

public class BotSavedLookEvent extends BotEvent {

    public HabboGender gender;


    public String newLook;


    public int effect;


    public BotSavedLookEvent(Bot bot, HabboGender gender, String newLook, int effect) {
        super(bot);

        this.gender = gender;
        this.newLook = newLook;
        this.effect = effect;
    }
}
