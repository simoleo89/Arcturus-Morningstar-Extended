package com.eu.arcturus.messages.rcon;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.generic.alerts.GenericAlertComposer;
import com.eu.arcturus.messages.outgoing.generic.alerts.StaffAlertWithLinkComposer;
import com.google.gson.Gson;

import java.util.Map;

public class HotelAlert extends RCONMessage<HotelAlert.JSONHotelAlert> {

    public HotelAlert() {
        super(JSONHotelAlert.class);
    }

    @Override
    public void handle(Gson gson, JSONHotelAlert object) {
        ServerMessage serverMessage;
        if (object.url.isEmpty()) {
            serverMessage = new GenericAlertComposer(object.message).compose();
        } else {
            serverMessage = new StaffAlertWithLinkComposer(object.message, object.url).compose();
        }

        if (serverMessage != null) {
            for (Map.Entry<Integer, Habbo> set : Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos().entrySet()) {
                Habbo habbo = set.getValue();
                if (habbo.getHabboStats().blockStaffAlerts)
                    continue;

                habbo.getClient().sendResponse(serverMessage);
            }
        }
    }

    static class JSONHotelAlert {

        public String message;


        public String url = "";
    }
}
