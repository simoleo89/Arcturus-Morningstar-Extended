package com.eu.habbo.habbohotel.modtool;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class CfhCategory {
    private final String name;
    private final Map<Integer, CfhTopic> topics;

    public CfhCategory(int id, String name) {
        this.name = name;
        this.topics = Collections.synchronizedMap(new HashMap<>());
    }

    public void addTopic(CfhTopic topic) {
        this.topics.put(topic.id, topic);
    }

    public Map<Integer, CfhTopic> getTopics() {
        return this.topics;
    }

    public String getName() {
        return this.name;
    }
}