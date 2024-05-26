package com.kfc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    static String databaseName = System.getenv("DB_URL");
    static String user = System.getenv("DB_USERNAME");
    static String password = System.getenv("DB_PASSWORD");
    static Connection connection;

    public static Connection createConnection() {
        try {
            if (connection == null) {
                return DriverManager.getConnection(databaseName, user, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
