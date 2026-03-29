package com.eu.habbo.test;

import com.eu.habbo.Emulator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Emulator utility methods.
 * These tests do not require a running server or database connection.
 */
class EmulatorUtilTest {

    @Test
    @DisplayName("timeStringToSeconds: parses single time unit")
    void timeStringToSeconds_singleUnit() {
        assertEquals(60, Emulator.timeStringToSeconds("1 minute"));
        assertEquals(3600, Emulator.timeStringToSeconds("1 hour"));
        assertEquals(86400, Emulator.timeStringToSeconds("1 day"));
        assertEquals(604800, Emulator.timeStringToSeconds("1 week"));
        assertEquals(31536000, Emulator.timeStringToSeconds("1 year"));
    }

    @Test
    @DisplayName("timeStringToSeconds: parses multiple time units")
    void timeStringToSeconds_multipleUnits() {
        assertEquals(3660, Emulator.timeStringToSeconds("1 hour 1 minute"));
        assertEquals(90061, Emulator.timeStringToSeconds("1 day 1 hour 1 minute 1 second"));
    }

    @Test
    @DisplayName("timeStringToSeconds: parses larger amounts")
    void timeStringToSeconds_largerAmounts() {
        assertEquals(300, Emulator.timeStringToSeconds("5 minute"));
        assertEquals(7200, Emulator.timeStringToSeconds("2 hour"));
        assertEquals(259200, Emulator.timeStringToSeconds("3 day"));
    }

    @Test
    @DisplayName("timeStringToSeconds: returns 0 for empty or invalid input")
    void timeStringToSeconds_invalidInput() {
        assertEquals(0, Emulator.timeStringToSeconds(""));
        assertEquals(0, Emulator.timeStringToSeconds("invalid"));
        assertEquals(0, Emulator.timeStringToSeconds("no numbers here"));
    }

    @Test
    @DisplayName("isNumeric: returns true for numeric strings")
    void isNumeric_valid() {
        assertTrue(Emulator.isNumeric("123"));
        assertTrue(Emulator.isNumeric("0"));
        assertTrue(Emulator.isNumeric("999999"));
    }

    @Test
    @DisplayName("isNumeric: returns false for non-numeric strings")
    void isNumeric_invalid() {
        assertFalse(Emulator.isNumeric("abc"));
        assertFalse(Emulator.isNumeric("12.3"));
        assertFalse(Emulator.isNumeric("12a"));
        assertFalse(Emulator.isNumeric(""));
        assertFalse(Emulator.isNumeric(null));
    }

    @Test
    @DisplayName("version string is correctly formatted")
    void versionFormat() {
        assertNotNull(Emulator.version);
        assertTrue(Emulator.version.contains("Arcturus Morningstar"));
        assertTrue(Emulator.version.contains(Emulator.MAJOR + "." + Emulator.MINOR + "." + Emulator.BUILD));
    }
}
