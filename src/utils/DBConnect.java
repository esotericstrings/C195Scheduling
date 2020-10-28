package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Safely connect with the WGU database to access application information
 */
public class DBConnect {
    /** Name of privileged user on database */
    private static final String user = "U06mc0";
    /** Password for privileged user on database */
    private static final String password = "53688808384";
    /** Database name */
    private static final String db_name = "WJ06mc0";
    /** Connection url built from database name */
    private static final String db_url = "jdbc:mysql://wgudb.ucertify.com/"+db_name+"?profileSQL=true";
    /** Database connection */
    public static Connection conn;

    /**
     * Creates a connection with the WGU database
     * @return A database connection to WGU database
     * @throws SQLException Fails to get database connection
     */
    public static Connection getConn() throws SQLException {
        conn = DriverManager.getConnection(db_url, user, password);
        return conn;
    }

    /**
     * Closes database connection
     * @throws SQLException Fails to close database connection
     */
    public static void close() throws SQLException {
        conn.close();
    }
}
