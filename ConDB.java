package tdt4140.gr1830.app.database;

import tdt4140.gr1830.app.utils.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// ConDB currently requiers a MySql server.

public class ConDB {
    // Fields are mostly kept for proper closing
    private static Connection connect = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;

    private static String userName = (String) Settings.getSetting("dbusername");
    private static String password = (String) Settings.getSetting("dbpassword");
    private static String serverName = (String) Settings.getSetting("dbhostname");
    private static int portNumber = (Integer) Settings.getSetting("dbportnumber");
    private static String dbName = (String) Settings.getSetting("dbname");

    private static boolean debug = (Boolean) Settings.getSetting("dbdebug");

    // Set up a connection and log in to the db specified by the fields
    public static void createConnection() {
        String adr = String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s", serverName, portNumber, dbName, userName, password);
        print("Connecting to database:\n" + adr);
        try {
            connect = DriverManager.getConnection(adr);

            print("Connected to database");
        }
        catch (Exception e){
            print("Error while connecting");
            e.printStackTrace();
        }
    }

    // Do a sql query to the db, return value for model
    public static List<String[]> query(String query){
        print("Query: " + query);
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            return handleResult();
        }
        catch (Exception e){
            print("Error executing query");
            e.printStackTrace();
        }
        return null;
    }

    // Return list of arrays consisting of the string values in the result
    private static List<String[]> handleResult() throws SQLException {
        List<String[]> result = new ArrayList<>();
        int colCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            String[] row = new String[colCount];
            for (int i = 0; i < colCount; i++){
                row[i] = resultSet.getString(i + 1);
            }
            result.add(row);
        }
        return result;
    }

    // Do a query that changes something in the db
    public static boolean update(String query) {
        try {
            statement = connect.createStatement();
            print("" + statement.executeUpdate(query));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Close everything
    public static void close() {
        print("Closing connection");
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
            print("Connection closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Debug printing
    protected static void print(String msg) {
        if (debug)
            System.out.println(msg);
    }

    public static void setDebug(boolean enable){
        debug = enable;
    }

}
