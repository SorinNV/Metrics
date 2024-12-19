package org.spbstu.dell.metrics.data.saving;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcConnection {
    private static final Logger LOGGER =
            Logger.getLogger(JdbcConnection.class.getName());
    private static Connection connection = null;

    private JdbcConnection() {
        throw new IllegalStateException("Utility class");
    }

    public static Connection getConnection() {
        if (connection == null) {
            String url = "jdbc:postgresql://212.111.87.144:5432/PostgreSQL-4183";
            String user = "user";
            String password = "6Z7JB5gYL40UD8&13";

            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }

        return connection;
    }
}
