package com.eu.arcturus.messages.incoming.navigator;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.navigator.PrivateRoomsComposer;

public class SearchRoomsByTagEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String tag = this.packet.readString();

        this.client.sendResponse(new PrivateRoomsComposer(Emulator.getGameEnvironment().getRoomManager().getRoomsWithTag(tag)));
    }
}
