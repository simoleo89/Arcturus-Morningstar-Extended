package com.eu.arcturus.plugin.events.guilds.forums;

import com.eu.arcturus.habbohotel.guilds.forums.ForumThread;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.Event;

public class GuildForumThreadCommentBeforeCreated extends Event {
    public final ForumThread thread;
    public final Habbo poster;
    public final String message;

    public GuildForumThreadCommentBeforeCreated(ForumThread thread, Habbo poster, String message) {
        this.thread = thread;
        this.poster = poster;
        this.message = message;
    }
}
