package com.eu.arcturus.plugin.events.users.subscriptions;

import com.eu.arcturus.habbohotel.users.subscriptions.Subscription;
import com.eu.arcturus.plugin.Event;

public class UserSubscriptionExpiredEvent extends Event {
    public final int userId;
    public final Subscription subscription;

    public UserSubscriptionExpiredEvent(int userId, Subscription subscription) {
        super();

        this.userId = userId;
        this.subscription = subscription;
    }
}
