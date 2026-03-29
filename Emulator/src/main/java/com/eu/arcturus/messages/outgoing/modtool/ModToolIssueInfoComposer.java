package com.eu.arcturus.messages.outgoing.modtool;

import com.eu.arcturus.habbohotel.modtool.ModToolIssue;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class ModToolIssueInfoComposer extends MessageComposer {
    private final ModToolIssue issue;

    public ModToolIssueInfoComposer(ModToolIssue issue) {
        this.issue = issue;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.ModToolIssueInfoComposer);
        this.issue.serialize(this.response);
        return this.response;
    }

    public ModToolIssue getIssue() {
        return issue;
    }
}
