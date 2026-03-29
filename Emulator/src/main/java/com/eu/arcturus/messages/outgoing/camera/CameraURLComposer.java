package com.eu.arcturus.messages.outgoing.camera;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class CameraURLComposer extends MessageComposer {
    private final String URL;

    public CameraURLComposer(String url) {
        this.URL = url;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.CameraURLComposer);
        this.response.appendString(this.URL);
        return this.response;
    }

    public String getURL() {
        return URL;
    }
}