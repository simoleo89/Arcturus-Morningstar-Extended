package com.eu.arcturus.plugin.events.guilds.forums;

import com.eu.arcturus.habbohotel.guilds.forums.ForumThread;
import com.eu.arcturus.plugin.Event;

public class GuildForumThreadCreated extends Event {
    public final ForumThread thread;

    public GuildForumThreadCreated(ForumThread thread) {
        this.thread = thread;
    }
}
