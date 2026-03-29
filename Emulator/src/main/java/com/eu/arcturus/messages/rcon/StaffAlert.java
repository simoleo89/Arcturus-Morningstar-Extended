package com.eu.arcturus.messages.rcon;

import com.eu.arcturus.Emulator;
import com.google.gson.Gson;

public class StaffAlert extends RCONMessage<StaffAlert.JSON> {
    public StaffAlert() {
        super(JSON.class);
    }

    @Override
    public void handle(Gson gson, JSON json) {
        Emulator.getGameEnvironment().getHabboManager().staffAlert(json.message);
    }

    static class JSON {

        public String message;
    }
}
