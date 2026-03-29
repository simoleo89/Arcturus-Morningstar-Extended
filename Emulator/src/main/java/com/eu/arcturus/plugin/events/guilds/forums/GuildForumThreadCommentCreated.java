package com.eu.arcturus.plugin.events.guilds.forums;

import com.eu.arcturus.habbohotel.guilds.forums.ForumThreadComment;
import com.eu.arcturus.plugin.Event;

public class GuildForumThreadCommentCreated extends Event {
    public final ForumThreadComment comment;

    public GuildForumThreadCommentCreated(ForumThreadComment comment) {
        this.comment = comment;
    }
}
