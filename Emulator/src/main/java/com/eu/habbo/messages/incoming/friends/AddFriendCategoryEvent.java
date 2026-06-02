package com.eu.habbo.messages.incoming.friends;

import com.eu.habbo.habbohotel.messenger.MessengerCategory;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.friends.MessengerInitComposer;

public class AddFriendCategoryEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        String name = this.packet.readString();
        Habbo habbo = this.client.getHabbo();

        if (habbo == null || name == null) return;

        name = name.trim();
        if (name.isEmpty() || name.length() > 25) return;
        if (habbo.getHabboInfo().getMessengerCategories().size() >= 20) return;

        for (MessengerCategory existing : habbo.getHabboInfo().getMessengerCategories()) {
            if (existing.getName().equalsIgnoreCase(name)) return;
        }

        MessengerCategory category = new MessengerCategory(name, habbo.getHabboInfo().getId(), 0);
        habbo.getHabboInfo().addMessengerCategory(category);

        this.client.sendResponse(new MessengerInitComposer(habbo));
    }
}
