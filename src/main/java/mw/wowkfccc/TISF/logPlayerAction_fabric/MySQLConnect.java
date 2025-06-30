package mw.wowkfccc.TISF.logPlayerAction_fabric;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnect {
    private HikariDataSource ds;

    private final String host = "localhost";
    private final int port = 3306;
    private final String database = "your_database";
    private final String username = "your_username";
    private final String password = "your_password";

    public void connect() {
        if (ds != null && !ds.isClosed()) return;

        String jdbcUrl = String.format(
                "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                host, port, database
        );

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(jdbcUrl);
        cfg.setUsername(username);
        cfg.setPassword(password);

        // Pool settings
        cfg.setMaximumPoolSize(10);
        cfg.setConnectionTimeout(30000);
        cfg.setIdleTimeout(600000);
        cfg.setMaxLifetime(30000);
        cfg.setValidationTimeout(5000);
        cfg.setKeepaliveTime(150000);

        ds = new HikariDataSource(cfg);
        System.out.println("[MySQLConnect] ✅ MySQL connection pool initialized");
    }

    public Connection getConnection() throws SQLException {
        if (ds == null || ds.isClosed()) {
            connect();
        }
        return ds.getConnection();
    }

    public boolean isConnected() {
        if (ds == null || ds.isClosed()) {
            return false;
        }
        try (Connection conn = ds.getConnection()) {
            return conn != null && conn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    public void disconnect() {
        if (ds != null && !ds.isClosed()) {
            ds.close();
            System.out.println("[MySQLConnect] 🛑 MySQL connection pool closed");
        }
    }
}
