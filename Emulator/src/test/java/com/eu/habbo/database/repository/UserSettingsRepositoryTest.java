package com.eu.habbo.database.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserSettingsRepositoryTest {

    // A column outside the allowlist must be rejected before any database
    // access happens, so this is verifiable without a live connection and
    // guards against SQL identifier injection.

    @Test
    void rejectsColumnOutsideAllowlist() {
        assertFalse(UserSettingsRepository.updateFlag(1, "password", true));
        assertFalse(UserSettingsRepository.updateFlag(1, "rank`; DROP TABLE users; --", true));
    }

    @Test
    void rejectionDoesNotThrow() {
        assertDoesNotThrow(() -> UserSettingsRepository.updateFlag(1, "not_a_real_column", false));
    }
}
