package com.aruiz.loans.shared.infrastructure.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2PoolFactory {
    private static Connection connection;
    public static Connection getConnection(String connectionString) throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(connectionString);
            // Using H2 memory, if connection is dropped/closed, then all data will be lost.
            // Running migrations from singleton helps to aid this issue.
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE loans(id INT NOT NULL, payments OBJECT ARRAY)");
            }
        }

        return connection;
    }
}
