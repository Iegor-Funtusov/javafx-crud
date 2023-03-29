package ua.com.alevel.persistence.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class JdbcService {

    private static final JdbcService jdbcService = new JdbcService();

    private Connection connection;
    private Statement statement;

    private JdbcService() { }

    public static JdbcService getInstance() {
        return jdbcService;
    }

    public void initDBState(Map<String, String> properiesMap) {
        try {
            Class.forName(properiesMap.get("jdbc.driver"));
            this.connection = DriverManager.getConnection(
                    properiesMap.get("jdbc.url"),
                    properiesMap.get("jdbc.username"),
                    properiesMap.get("jdbc.password"));
            this.statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("e = " + e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }
}
