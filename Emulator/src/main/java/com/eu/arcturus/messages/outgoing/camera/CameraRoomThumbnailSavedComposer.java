package com.eu.arcturus.messages.outgoing.camera;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class CameraRoomThumbnailSavedComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.CameraRoomThumbnailSavedComposer);
        return this.response;
    }
}