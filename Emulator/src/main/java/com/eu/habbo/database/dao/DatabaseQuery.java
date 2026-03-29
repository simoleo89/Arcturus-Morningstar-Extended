package com.eu.habbo.database.dao;

import com.eu.habbo.Emulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Lightweight database access utility that reduces JDBC boilerplate.
 * Wraps the repetitive try-with-resources / PreparedStatement pattern
 * used across 128+ files in the codebase.
 *
 * Usage examples:
 *
 *   // Simple query returning one result
 *   Optional<String> name = DatabaseQuery.queryOne(
 *       "SELECT username FROM users WHERE id = ?",
 *       stmt -> stmt.setInt(1, userId),
 *       rs -> rs.getString("username")
 *   );
 *
 *   // Query returning a list
 *   List<Integer> ids = DatabaseQuery.queryList(
 *       "SELECT id FROM rooms WHERE owner_id = ?",
 *       stmt -> stmt.setInt(1, ownerId),
 *       rs -> rs.getInt("id")
 *   );
 *
 *   // Update/insert with no return
 *   DatabaseQuery.update(
 *       "UPDATE users SET online = ? WHERE id = ?",
 *       stmt -> { stmt.setInt(1, 0); stmt.setInt(2, userId); }
 *   );
 *
 *   // Insert with generated key
 *   int newId = DatabaseQuery.insertAndGetKey(
 *       "INSERT INTO rooms (name, owner_id) VALUES (?, ?)",
 *       stmt -> { stmt.setString(1, name); stmt.setInt(2, ownerId); }
 *   );
 */
public final class DatabaseQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseQuery.class);

    private DatabaseQuery() {}

    @FunctionalInterface
    public interface StatementPreparer {
        void prepare(PreparedStatement stmt) throws SQLException;
    }

    @FunctionalInterface
    public interface ResultMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    /**
     * Execute a SELECT query and return the first result, or empty if no rows.
     */
    public static <T> Optional<T> queryOne(String sql, StatementPreparer preparer, ResultMapper<T> mapper) {
        try (Connection conn = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            preparer.prepare(stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Query failed: {}", sql, e);
        }
        return Optional.empty();
    }

    /**
     * Execute a SELECT query and return all results as a list.
     */
    public static <T> List<T> queryList(String sql, StatementPreparer preparer, ResultMapper<T> mapper) {
        List<T> results = new ArrayList<>();
        try (Connection conn = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            preparer.prepare(stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Query failed: {}", sql, e);
        }
        return results;
    }

    /**
     * Execute a SELECT with no parameters.
     */
    public static <T> List<T> queryList(String sql, ResultMapper<T> mapper) {
        return queryList(sql, stmt -> {}, mapper);
    }

    /**
     * Execute an UPDATE, INSERT, or DELETE statement.
     * Returns the number of affected rows.
     */
    public static int update(String sql, StatementPreparer preparer) {
        try (Connection conn = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            preparer.prepare(stmt);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Update failed: {}", sql, e);
        }
        return 0;
    }

    /**
     * Execute an INSERT and return the generated key.
     * Returns -1 if insert fails or no key generated.
     */
    public static int insertAndGetKey(String sql, StatementPreparer preparer) {
        try (Connection conn = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparer.prepare(stmt);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Insert failed: {}", sql, e);
        }
        return -1;
    }

    /**
     * Execute a batch of statements within a single connection.
     * Useful for bulk operations.
     */
    public static void batch(String sql, Consumer<PreparedStatement> batchPreparer) {
        try (Connection conn = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            batchPreparer.accept(stmt);
            stmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            LOGGER.error("Batch failed: {}", sql, e);
        }
    }

    /**
     * Execute raw SQL with a provided connection (for transactional use).
     */
    public static <T> Optional<T> queryOneWithConnection(Connection conn, String sql, StatementPreparer preparer, ResultMapper<T> mapper) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            preparer.prepare(stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(mapper.map(rs));
                }
            }
        }
        return Optional.empty();
    }
}
