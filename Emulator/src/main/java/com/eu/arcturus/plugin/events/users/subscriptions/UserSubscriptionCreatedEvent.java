package com.eu.arcturus.plugin.events.users.subscriptions;

import com.eu.arcturus.plugin.Event;

public class UserSubscriptionCreatedEvent extends Event {
    public final int userId;
    public final String subscriptionType;
    public final int duration;

    public UserSubscriptionCreatedEvent(int userId, String subscriptionType, int duration) {
        super();

        this.userId = userId;
        this.subscriptionType = subscriptionType;
        this.duration = duration;
    }
}
