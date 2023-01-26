package com.aruiz.loans.shared.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2PoolFactory {
    private static Connection connection;
    public static Connection getConnection(String connectionString) throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(connectionString);
        }

        return connection;
    }
}
