package com.eu.arcturus.messages.incoming.camera;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.camera.CameraPriceComposer;

public class RequestCameraConfigurationEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(new CameraPriceComposer(Emulator.getConfig().getInt("camera.price.credits"), Emulator.getConfig().getInt("camera.price.points"), Emulator.getConfig().getInt("camera.price.points.publish")));
    }
}