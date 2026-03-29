package com.eu.arcturus.plugin.events.guilds.forums;

import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.Event;

public class GuildForumThreadBeforeCreated extends Event {
    public final Guild guild;
    public final Habbo opener;
    public final String subject;
    public final String message;

    public GuildForumThreadBeforeCreated(Guild guild, Habbo opener, String subject, String message) {
        this.guild = guild;
        this.opener = opener;
        this.subject = subject;
        this.message = message;
    }
}
