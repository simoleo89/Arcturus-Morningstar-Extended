package com.eu.arcturus.threading.runnables;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.users.HabboItem;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QueryDeleteHabboItems implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryDeleteHabboItems.class);

    private Map<Integer, HabboItem> items;

    public QueryDeleteHabboItems(Map<Integer, HabboItem> items) {
        this.items = items;
    }

    @Override
    public void run() {
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement("DELETE FROM items WHERE id = ?")) {
            for (HabboItem item : this.items.values()) {
                if (item.getRoomId() > 0)
                    continue;

                statement.setInt(1, item.getId());
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("Caught SQL exception", e);
        }

        this.items.clear();
    }
}
