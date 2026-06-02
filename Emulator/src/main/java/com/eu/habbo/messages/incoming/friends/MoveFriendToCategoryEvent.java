package com.eu.habbo.messages.incoming.friends;

import com.eu.habbo.habbohotel.messenger.MessengerBuddy;
import com.eu.habbo.habbohotel.messenger.MessengerCategory;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.friends.UpdateFriendComposer;

public class MoveFriendToCategoryEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int friendId = this.packet.readInt();
        int categoryId = this.packet.readInt();
        Habbo habbo = this.client.getHabbo();

        if (habbo == null) return;

        MessengerBuddy buddy = habbo.getMessenger().getFriends().get(friendId);
        if (buddy == null) return;

        if (categoryId != 0) {
            boolean exists = false;
            for (MessengerCategory category : habbo.getHabboInfo().getMessengerCategories()) {
                if (category.getId() == categoryId) {
                    exists = true;
                    break;
                }
            }
            if (!exists) return;
        }

        buddy.setCategoryId(categoryId);
        this.client.sendResponse(new UpdateFriendComposer(habbo, buddy, 0));
    }
}
