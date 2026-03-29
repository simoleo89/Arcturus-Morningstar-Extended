package com.eu.arcturus.messages.incoming.users;

import com.eu.arcturus.habbohotel.users.subscriptions.SubscriptionHabboClub;
import com.eu.arcturus.messages.incoming.MessageHandler;

public class RequestClubCenterEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        this.client.sendResponse(SubscriptionHabboClub.calculatePayday(this.client.getHabbo().getHabboInfo()));
    }
}
