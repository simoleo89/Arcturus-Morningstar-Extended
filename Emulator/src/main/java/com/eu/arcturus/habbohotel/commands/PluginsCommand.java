package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.messages.outgoing.generic.alerts.MessagesForYouComposer;
import com.eu.arcturus.plugin.HabboPlugin;

import java.util.Collections;

public class PluginsCommand extends Command {
    public PluginsCommand() {
        super(null, Emulator.getTexts().getValue("commands.keys.cmd_plugins").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        StringBuilder message = new StringBuilder("Plugins (" + Emulator.getPluginManager().getPlugins().size() + ")\r");

        for (HabboPlugin plugin : Emulator.getPluginManager().getPlugins()) {
            message.append("\r").append(plugin.configuration.name).append(" By ").append(plugin.configuration.author);
        }


        if (Emulator.getConfig().getBoolean("commands.plugins.oldstyle")) {
            gameClient.sendResponse(new MessagesForYouComposer(Collections.singletonList(message.toString())));
        } else {
            gameClient.getHabbo().alert(message.toString());
        }

        return true;
    }
}
