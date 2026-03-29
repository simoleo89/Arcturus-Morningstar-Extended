package com.eu.arcturus.messages.outgoing.friends;

import com.eu.arcturus.habbohotel.messenger.Messenger;
import com.eu.arcturus.habbohotel.messenger.MessengerCategory;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.List;

public class MessengerInitComposer extends MessageComposer {
    private final Habbo habbo;

    public MessengerInitComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {

        this.response.init(Outgoing.MessengerInitComposer);
        if (this.habbo.hasPermission("acc_infinite_friends")) {
            this.response.appendInt(Integer.MAX_VALUE);
            this.response.appendInt(1337);
            this.response.appendInt(Integer.MAX_VALUE);
        } else {
            this.response.appendInt(Messenger.MAXIMUM_FRIENDS);
            this.response.appendInt(1337);
            this.response.appendInt(Messenger.MAXIMUM_FRIENDS_HC);
        }
        if (!this.habbo.getHabboInfo().getMessengerCategories().isEmpty()) {

            List<MessengerCategory> messengerCategories = this.habbo.getHabboInfo().getMessengerCategories();
            this.response.appendInt(messengerCategories.size());

            for (MessengerCategory mc : messengerCategories) {
                this.response.appendInt(mc.getId());
                this.response.appendString(mc.getName());
            }
        } else {
            this.response.appendInt(0);
        }
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}

