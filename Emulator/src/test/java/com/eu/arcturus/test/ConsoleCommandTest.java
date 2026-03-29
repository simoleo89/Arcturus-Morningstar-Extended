package com.eu.arcturus.test;

import com.eu.arcturus.core.consolecommands.ConsoleCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ConsoleCommand registry and dispatch system.
 */
class ConsoleCommandTest {

    @BeforeAll
    static void setup() {
        ConsoleCommand.load();
    }

    @Test
    @DisplayName("All default commands are registered")
    void allDefaultCommandsRegistered() {
        assertNotNull(ConsoleCommand.findCommand("stop"));
        assertNotNull(ConsoleCommand.findCommand("info"));
        assertNotNull(ConsoleCommand.findCommand("test"));
        assertNotNull(ConsoleCommand.findCommand("interactions"));
        assertNotNull(ConsoleCommand.findCommand("rconcommands"));
        assertNotNull(ConsoleCommand.findCommand("thankyou"));
    }

    @Test
    @DisplayName("findCommand returns null for unknown commands")
    void findCommand_unknown() {
        assertNull(ConsoleCommand.findCommand("nonexistent"));
        assertNull(ConsoleCommand.findCommand(""));
    }

    @Test
    @DisplayName("handle returns false for unknown commands")
    void handle_unknownCommand() {
        assertFalse(ConsoleCommand.handle("totally_unknown_command"));
    }

    @Test
    @DisplayName("Commands have non-empty key and usage")
    void commandsHaveKeyAndUsage() {
        ConsoleCommand cmd = ConsoleCommand.findCommand("info");
        assertNotNull(cmd);
        assertFalse(cmd.key.isEmpty());
        assertFalse(cmd.usage.isEmpty());
    }
}
