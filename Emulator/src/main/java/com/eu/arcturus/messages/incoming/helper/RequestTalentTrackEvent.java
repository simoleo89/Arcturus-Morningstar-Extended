package com.eu.arcturus.messages.incoming.helper;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.achievements.TalentTrackType;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.achievements.talenttrack.TalentTrackComposer;

public class RequestTalentTrackEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        if (Emulator.getConfig().getBoolean("hotel.talenttrack.enabled")) {
            this.client.sendResponse(new TalentTrackComposer(this.client.getHabbo(), TalentTrackType.valueOf(this.packet.readString().toUpperCase())));
        }
    }
}
