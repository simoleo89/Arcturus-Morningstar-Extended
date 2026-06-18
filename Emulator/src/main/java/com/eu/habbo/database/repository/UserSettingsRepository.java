package com.eu.habbo.database.repository;

import com.eu.habbo.Emulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

/**
 * Repository for the {@code users_settings} table.
 *
 * <p>This is the first example of the DAO/repository pattern the codebase
 * should adopt: it centralizes the SQL and JDBC plumbing for one table so the
 * domain classes (e.g. {@code HabboStats}) no longer embed raw queries. As more
 * call sites are migrated, the scattered inline JDBC shrinks and the SQL becomes
 * auditable in one place.
 */
public final class UserSettingsRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSettingsRepository.class);

    // Boolean flag columns this repository is allowed to write. The column name
    // is interpolated into the SQL (JDBC cannot parameterize an identifier), so
    // it is validated against this allowlist to keep the statement safe.
    private static final Set<String> WRITABLE_FLAG_COLUMNS =
            Set.of("mentions_enabled", "mass_mentions_enabled");

    private UserSettingsRepository() {
    }

    /**
     * Persists a boolean flag column for a user. No-ops (and logs an error) if
     * the column is not in the allowlist.
     *
     * @return {@code true} if a row was updated.
     */
    public static boolean updateFlag(int userId, String column, boolean enabled) {
        if (!WRITABLE_FLAG_COLUMNS.contains(column)) {
            LOGGER.error("Refusing to persist unknown users_settings column '{}'", column);
            return false;
        }
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE users_settings SET `" + column + "` = ? WHERE user_id = ? LIMIT 1")) {
            statement.setString(1, enabled ? "1" : "0");
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Failed to persist users_settings.{} for user {}", column, userId, e);
            return false;
        }
    }
}
