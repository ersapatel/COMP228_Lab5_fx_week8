package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JDBCConnector {

    private static final String URL  = "jdbc:mysql://127.0.0.1:3306/week9_LabWork_SwapnilPatel";
    private static final String USER = "root";
    private static final String PASS = "Admin123";

    private JDBCConnector() {}

    public static Connection get() throws SQLException {
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        return con;
    }
}
