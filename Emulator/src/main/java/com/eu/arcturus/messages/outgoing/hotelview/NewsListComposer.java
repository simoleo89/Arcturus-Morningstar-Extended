package com.eu.arcturus.messages.outgoing.hotelview;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.hotelview.NewsList;
import com.eu.arcturus.habbohotel.hotelview.NewsWidget;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class NewsListComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.NewsWidgetsComposer);
        NewsList newsList = Emulator.getGameEnvironment().getHotelViewManager().getNewsList();

        this.response.appendInt(newsList.getNewsWidgets().size());

        for (NewsWidget widget : newsList.getNewsWidgets()) {
            this.response.appendInt(widget.getId());
            this.response.appendString(widget.getTitle());
            this.response.appendString(widget.getMessage());
            this.response.appendString(widget.getButtonMessage());
            this.response.appendInt(widget.getType());
            this.response.appendString(widget.getLink());
            this.response.appendString(widget.getImage());
        }

        return this.response;
    }
}
