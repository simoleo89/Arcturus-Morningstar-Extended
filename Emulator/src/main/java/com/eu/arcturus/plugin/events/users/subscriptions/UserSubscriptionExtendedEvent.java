package com.eu.arcturus.plugin.events.users.subscriptions;

import com.eu.arcturus.habbohotel.users.subscriptions.Subscription;
import com.eu.arcturus.plugin.Event;

public class UserSubscriptionExtendedEvent extends Event {
    public final int userId;
    public final Subscription subscription;
    public final int duration;

    public UserSubscriptionExtendedEvent(int userId, Subscription subscription, int duration) {
        super();
        this.userId = userId;
        this.subscription = subscription;
        this.duration = duration;
    }
}
