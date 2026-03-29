package com.eu.arcturus.messages.outgoing.navigator;

import com.eu.arcturus.habbohotel.navigation.EventCategory;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.ArrayList;
import java.util.List;

public class NewNavigatorEventCategoriesComposer extends MessageComposer {
    public static List<EventCategory> CATEGORIES = new ArrayList<>();

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.NewNavigatorEventCategoriesComposer);

        this.response.appendInt(NewNavigatorEventCategoriesComposer.CATEGORIES.size());

        for (EventCategory category : NewNavigatorEventCategoriesComposer.CATEGORIES) {
            category.serialize(this.response);
        }

        return this.response;
    }
}
