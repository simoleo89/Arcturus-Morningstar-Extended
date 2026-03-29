package com.eu.arcturus.messages.outgoing.unknown;

import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.Map;

public class UnknownPollQuestionComposer extends MessageComposer {
    private final int unknownInt;
    private final Map<String, Integer> unknownMap;

    public UnknownPollQuestionComposer(int unknownInt, Map<String, Integer> unknownMap) {
        this.unknownInt = unknownInt;
        this.unknownMap = unknownMap;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.SimplePollAnswersComposer);
        this.response.appendInt(this.unknownInt);
        this.response.appendInt(this.unknownMap.size());
        for (Map.Entry<String, Integer> entry : this.unknownMap.entrySet()) {
            this.response.appendString(entry.getKey());
            this.response.appendInt(entry.getValue());
        }
        return this.response;
    }

    public int getUnknownInt() {
        return unknownInt;
    }

    public Map<String, Integer> getUnknownMap() {
        return unknownMap;
    }
}