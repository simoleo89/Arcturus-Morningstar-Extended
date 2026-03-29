package com.eu.habbo.messages.outgoing.modtool;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.modtool.CfhCategory;
import com.eu.habbo.habbohotel.modtool.CfhTopic;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;
import java.util.function.Consumer;

public class CfhTopicsMessageComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.CfhTopicsMessageComposer);

        this.response.appendInt(Emulator.getGameEnvironment().getModToolManager().getCfhCategories().values().size());

        Emulator.getGameEnvironment().getModToolManager().getCfhCategories().values().forEach(new Consumer<CfhCategory>() {
            @Override
            public void accept(CfhCategory category) {
                CfhTopicsMessageComposer.this.response.appendString(category.getName());
                CfhTopicsMessageComposer.this.response.appendInt(category.getTopics().values().size());
                category.getTopics().values().forEach(new Consumer<CfhTopic>() {
                    @Override
                    public void accept(CfhTopic topic) {
                        CfhTopicsMessageComposer.this.response.appendString(topic.name);
                        CfhTopicsMessageComposer.this.response.appendInt(topic.id);
                        CfhTopicsMessageComposer.this.response.appendString(topic.action.toString());
                    }
                });
            }
        });

        return this.response;
    }
}