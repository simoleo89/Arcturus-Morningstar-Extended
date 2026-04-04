package com.arcturus.plugin.furnifix;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.commands.CommandHandler;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.plugin.EventHandler;
import com.eu.habbo.plugin.EventListener;
import com.eu.habbo.plugin.HabboPlugin;
import com.eu.habbo.plugin.events.emulator.EmulatorLoadedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FurniInteractionFixerPlugin extends HabboPlugin implements EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FurniInteractionFixerPlugin.class);

    @Override
    public void onEnable() throws Exception {
        Emulator.getPluginManager().registerEvents(this, this);
        LOGGER.info("[FurniInteractionFixer] Plugin enabled! Waiting for emulator to load...");
    }

    @EventHandler
    public void onEmulatorLoaded(EmulatorLoadedEvent event) {
        // Register the in-game command
        CommandHandler.addCommand(new FixFurniCommand());

        // Register the console command
        com.eu.habbo.core.consolecommands.ConsoleCommand.addCommand(new FixFurniConsoleCommand());

        // Insert default texts if missing
        insertDefaultTexts();

        LOGGER.info("[FurniInteractionFixer] Loaded successfully!");
        LOGGER.info("[FurniInteractionFixer] In-game command: :fixfurni <scan|fix|unregistered|fixid>");
        LOGGER.info("[FurniInteractionFixer] Console command: fixinteractions <scan|fix|unregistered>");
    }

    private void insertDefaultTexts() {
        try {
            // Ensure the command key text exists
            String existing = Emulator.getTexts().getValue("commands.keys.cmd_fix_furni_interactions");
            if (existing == null || existing.isEmpty()) {
                Emulator.getTexts().register("commands.keys.cmd_fix_furni_interactions", "fixfurni");
            }
        } catch (Exception e) {
            LOGGER.warn("[FurniInteractionFixer] Could not register text keys, using defaults.", e);
        }
    }

    @Override
    public void onDisable() throws Exception {
        LOGGER.info("[FurniInteractionFixer] Plugin disabled.");
    }

    @Override
    public boolean hasPermission(Habbo habbo, String key) {
        // Delegate to default permission system
        return false;
    }
}
