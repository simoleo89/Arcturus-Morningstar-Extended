package com.eu.arcturus.habbohotel.modtool;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;

public class ScripterManager {
    public static void scripterDetected(GameClient client, String reason) {
        ScripterEvent scripterEvent = new ScripterEvent(client.getHabbo(), reason);
        Emulator.getPluginManager().fireEvent(scripterEvent);

        if (scripterEvent.isCancelled()) return;

        if (Emulator.getConfig().getBoolean("scripter.modtool.tickets", true)) {
            Emulator.getGameEnvironment().getModToolManager().quickTicket(client.getHabbo(), "Scripter", reason);
        }
    }
}
