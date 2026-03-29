package com.eu.arcturus.messages.incoming.rooms.items.jukebox;

import com.eu.arcturus.habbohotel.rooms.TraxManager;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.rooms.items.jukebox.JukeBoxMySongsComposer;
import com.eu.arcturus.messages.outgoing.rooms.items.jukebox.JukeBoxPlayListComposer;

public class JukeBoxEventOne extends MessageHandler {
    @Override
    public void handle() throws Exception {
        TraxManager traxManager = this.client.getHabbo().getHabboInfo().getCurrentRoom().getTraxManager();
        this.client.sendResponse(new JukeBoxPlayListComposer(traxManager.getSongs(), traxManager.totalLength()));
        this.client.sendResponse(new JukeBoxMySongsComposer(traxManager.myList(this.client.getHabbo())));
        this.client.getHabbo().getHabboInfo().getCurrentRoom().getTraxManager().updateCurrentPlayingSong(this.client.getHabbo());
    }
}
