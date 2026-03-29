package com.eu.arcturus.messages.outgoing.guilds.forums;

import com.eu.arcturus.habbohotel.guilds.forums.ForumThread;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuildForumThreadMessagesComposer extends MessageComposer {
    public final ForumThread thread;

    public GuildForumThreadMessagesComposer(ForumThread thread) {
        this.thread = thread;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GuildForumThreadMessagesComposer);
        this.response.appendInt(this.thread.getGuildId());
        this.thread.serialize(this.response);
        return this.response;
    }

    public ForumThread getThread() {
        return thread;
    }
}