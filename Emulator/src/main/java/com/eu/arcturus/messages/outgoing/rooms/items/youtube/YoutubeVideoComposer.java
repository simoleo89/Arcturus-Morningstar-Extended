package com.eu.arcturus.messages.outgoing.rooms.items.youtube;

import com.eu.arcturus.habbohotel.items.YoutubeManager;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class YoutubeVideoComposer extends MessageComposer {
    private final int itemId;
    private final YoutubeManager.YoutubeVideo video;
    private final boolean playing;
    private final int startTime;

    public YoutubeVideoComposer(int itemId, YoutubeManager.YoutubeVideo video, boolean playing, int startTime) {
        this.itemId = itemId;
        this.video = video;
        this.playing = playing;
        this.startTime = startTime;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.YoutubeMessageComposer2);
        this.response.appendInt(this.itemId);
        this.response.appendString(this.video.getId());
        this.response.appendInt(this.startTime);
        this.response.appendInt(this.video.getDuration());
        this.response.appendInt(this.playing ? 1 : 2);
        return this.response;
    }

    public int getItemId() {
        return itemId;
    }

    public YoutubeManager.YoutubeVideo getVideo() {
        return video;
    }

    public boolean isPlaying() {
        return playing;
    }

    public int getStartTime() {
        return startTime;
    }
}