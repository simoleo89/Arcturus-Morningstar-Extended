package com.eu.arcturus.core.consolecommands;

import com.eu.arcturus.habbohotel.commands.ShutdownCommand;

public class ConsoleShutdownCommand extends ConsoleCommand {
    public ConsoleShutdownCommand() {
        super("stop", "Stop the emulator.");
    }

    @Override
    public void handle(String[] args) throws Exception {
        new ShutdownCommand().handle(null, args);
    }
}