package com.eu.habbo.test;

import com.eu.habbo.database.dao.DatabaseQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the DatabaseQuery DAO utility.
 * These tests verify the API contracts without requiring a database connection.
 */
class DatabaseQueryTest {

    @Test
    @DisplayName("StatementPreparer functional interface compiles with lambda")
    void statementPreparerLambda() {
        DatabaseQuery.StatementPreparer preparer = stmt -> stmt.setInt(1, 42);
        assertNotNull(preparer);
    }

    @Test
    @DisplayName("ResultMapper functional interface compiles with lambda")
    void resultMapperLambda() {
        DatabaseQuery.ResultMapper<String> mapper = rs -> rs.getString("name");
        assertNotNull(mapper);
    }

    @Test
    @DisplayName("DatabaseQuery class cannot be instantiated")
    void cannotInstantiate() {
        var constructors = DatabaseQuery.class.getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertFalse(constructors[0].canAccess(null));
    }
}
