package com.eu.habbo.messages.incoming.friends;

import com.eu.habbo.habbohotel.messenger.MessengerCategory;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.friends.MessengerInitComposer;

public class RenameFriendCategoryEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int categoryId = this.packet.readInt();
        String name = this.packet.readString();
        Habbo habbo = this.client.getHabbo();

        if (habbo == null || name == null) return;

        name = name.trim();
        if (name.isEmpty() || name.length() > 25) return;

        boolean found = false;
        for (MessengerCategory category : habbo.getHabboInfo().getMessengerCategories()) {
            if (category.getId() == categoryId) {
                found = true;
                break;
            }
        }
        if (!found) return;

        habbo.getHabboInfo().renameMessengerCategory(categoryId, name);

        this.client.sendResponse(new MessengerInitComposer(habbo));
    }
}
