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
        // Register commands
        CommandHandler.addCommand(new FixFurniCommand());
        com.eu.habbo.core.consolecommands.ConsoleCommand.addCommand(new FixFurniConsoleCommand());

        // Insert default texts if missing
        insertDefaultTexts();

        // Learn prefixes from DB
        InteractionTypeFixer.refreshLearning();

        // Auto-fix on startup if configured
        boolean autoFix = false;
        try {
            String val = Emulator.getConfig().getValue("furnifix.autofix.enabled", "false");
            autoFix = val.equalsIgnoreCase("true") || val.equals("1");
        } catch (Exception ignored) {}

        if (autoFix) {
            LOGGER.info("[FurniInteractionFixer] Auto-fix enabled, running fixAll...");
            InteractionTypeFixer.FixSummary summary = InteractionTypeFixer.fixAll();
            if (summary.totalFixed > 0) {
                LOGGER.info("[FurniInteractionFixer] Auto-fix applied {} fixes.", summary.totalFixed);
                summary.fixCountByType.forEach((type, count) ->
                        LOGGER.info("[FurniInteractionFixer]   {} -> {} items", type, count));
                Emulator.getGameEnvironment().getItemManager().loadItems();
            } else {
                LOGGER.info("[FurniInteractionFixer] Auto-fix: no fixes needed.");
            }
        }

        LOGGER.info("[FurniInteractionFixer] Loaded successfully!");
        LOGGER.info("[FurniInteractionFixer] In-game:  :fixfurni <scan|fix|fixunreg|fixall|unregistered|stats|fixid>");
        LOGGER.info("[FurniInteractionFixer] Console:  fixinteractions <scan|fix|fixunreg|fixall|unregistered|stats>");
    }

    private void insertDefaultTexts() {
        try {
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
        return false;
    }
}
