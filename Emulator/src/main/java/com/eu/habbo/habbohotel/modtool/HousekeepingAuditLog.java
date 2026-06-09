package com.eu.habbo.habbohotel.modtool;

import com.eu.habbo.Emulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Append-only audit trail for privileged housekeeping/admin actions (rank grants,
 * currency grants, etc.). There was previously no record of which operator did
 * what to whom. Writes are dispatched off the calling thread; the backing table
 * is created on first use so no manual migration is required.
 */
public final class HousekeepingAuditLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(HousekeepingAuditLog.class);

    private static volatile boolean tableReady = false;

    private HousekeepingAuditLog() {
    }

    /**
     * Records a privileged action asynchronously.
     *
     * @param operatorId   the acting staff member's user id
     * @param operatorName the acting staff member's username
     * @param action       a short action key, e.g. {@code "user.set_rank"}
     * @param targetUserId the affected user's id (0 if not applicable)
     * @param detail       free-form detail, e.g. {@code "rankId=6"} (capped to 512 chars)
     * @param ip           the operator's IP, for correlation
     */
    public static void log(int operatorId, String operatorName, String action, int targetUserId, String detail, String ip) {
        Emulator.getThreading().run(() -> writeEntry(operatorId, operatorName, action, targetUserId, detail, ip));
    }

    private static void writeEntry(int operatorId, String operatorName, String action, int targetUserId, String detail, String ip) {
        ensureTable();

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO housekeeping_log (operator_id, operator_name, action, target_user_id, detail, ip, timestamp) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, operatorId);
            statement.setString(2, operatorName != null ? operatorName : "");
            statement.setString(3, action != null ? action : "");
            statement.setInt(4, targetUserId);
            statement.setString(5, truncate(detail));
            statement.setString(6, ip != null ? ip : "");
            statement.setInt(7, Emulator.getIntUnixTimestamp());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Failed to write housekeeping audit log entry", e);
        }
    }

    private static String truncate(String detail) {
        if (detail == null) return "";
        return detail.length() > 512 ? detail.substring(0, 512) : detail;
    }

    private static void ensureTable() {
        if (tableReady) {
            return;
        }
        synchronized (HousekeepingAuditLog.class) {
            if (tableReady) {
                return;
            }
            try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
                 Statement statement = connection.createStatement()) {
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS housekeeping_log (" +
                                "id INT UNSIGNED NOT NULL AUTO_INCREMENT, " +
                                "operator_id INT NOT NULL, " +
                                "operator_name VARCHAR(64) NOT NULL DEFAULT '', " +
                                "action VARCHAR(64) NOT NULL, " +
                                "target_user_id INT NOT NULL DEFAULT 0, " +
                                "detail VARCHAR(512) NOT NULL DEFAULT '', " +
                                "ip VARCHAR(64) NOT NULL DEFAULT '', " +
                                "timestamp INT NOT NULL, " +
                                "PRIMARY KEY (id), " +
                                "KEY idx_operator (operator_id), " +
                                "KEY idx_target (target_user_id), " +
                                "KEY idx_timestamp (timestamp)" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
                tableReady = true;
            } catch (SQLException e) {
                LOGGER.error("Failed to create housekeeping_log table", e);
            }
        }
    }
}
