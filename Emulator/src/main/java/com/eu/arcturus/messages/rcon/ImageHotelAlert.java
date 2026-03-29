package com.eu.arcturus.messages.rcon;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.generic.alerts.BubbleAlertComposer;
import com.google.gson.Gson;
import java.util.HashMap;

import java.util.Map;

public class ImageHotelAlert extends RCONMessage<ImageHotelAlert.JSON> {
    public ImageHotelAlert() {
        super(ImageHotelAlert.JSON.class);
    }

    @Override
    public void handle(Gson gson, JSON json) {
        HashMap<String, String> keys = new HashMap<>();

        if (!json.message.isEmpty()) {
            keys.put("message", json.message);
        }

        if (!json.url.isEmpty()) {
            keys.put("linkUrl", json.url);
        }

        if (!json.url_message.isEmpty()) {
            keys.put("linkTitle", json.url_message);
        }

        if (!json.title.isEmpty()) {
            keys.put("title", json.title);
        }

        if (!json.display_type.isEmpty()) {
            keys.put("display", json.display_type);
        }

        if (!json.image.isEmpty()) {
            keys.put("image", json.image);
        }

        ServerMessage message = new BubbleAlertComposer(json.bubble_key, keys).compose();

        for (Map.Entry<Integer, Habbo> set : Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos().entrySet()) {
            Habbo habbo = set.getValue();
            if (habbo.getHabboStats().blockStaffAlerts)
                continue;

            habbo.getClient().sendResponse(message);
        }
    }

    static class JSON {

        public String bubble_key = "";


        public String message = "";


        public String url = "";


        public String url_message = "";


        public String title = "";


        public String display_type = "";


        public String image = "";
    }
}