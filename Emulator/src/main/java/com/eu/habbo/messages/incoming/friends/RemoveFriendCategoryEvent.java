package com.eu.habbo.messages.incoming.friends;

import com.eu.habbo.habbohotel.messenger.MessengerBuddy;
import com.eu.habbo.habbohotel.messenger.MessengerCategory;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.friends.MessengerInitComposer;
import com.eu.habbo.messages.outgoing.friends.UpdateFriendComposer;

public class RemoveFriendCategoryEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int categoryId = this.packet.readInt();
        Habbo habbo = this.client.getHabbo();

        if (habbo == null) return;

        MessengerCategory target = null;
        for (MessengerCategory category : habbo.getHabboInfo().getMessengerCategories()) {
            if (category.getId() == categoryId) {
                target = category;
                break;
            }
        }
        if (target == null) return;

        habbo.getHabboInfo().deleteMessengerCategory(target);

        for (MessengerBuddy buddy : habbo.getMessenger().getFriends().values()) {
            if (buddy.getCategoryId() == categoryId) {
                buddy.setCategoryId(0);
                this.client.sendResponse(new UpdateFriendComposer(habbo, buddy, 0));
            }
        }

        this.client.sendResponse(new MessengerInitComposer(habbo));
    }
}
