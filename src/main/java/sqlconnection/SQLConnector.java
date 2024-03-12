package sqlconnection;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
/**
 * The SQLConnector class provides methods for connecting to a database, executing SQL queries,
 * and managing tables related to image and user information.
 * <p>
 * This class utilizes a properties file (db.properties) for database configuration.
 *
 * @author Daniel Hahn
 */
public class SQLConnector {

    private Connection connection;

    /**
     * Constructs a new SQLConnector object, establishing a connection to the database
     * using the properties specified in the "db.properties" file.
     *
     * @throws SQLException If a database access error occurs or the URL is null
     */
    public SQLConnector() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find properties");
                return;
            }

            // Load a properties file from class path, inside static method
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String connectionUrl = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        this.connection = DriverManager.getConnection(connectionUrl, user, password);
    }

    /**
     * Checks if a table with the specified name exists in the connected database.
     *
     * @param tableName The name of the table to check for existence.
     * @return {@code true} if the table exists, {@code false} otherwise.
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     */
    public Boolean checkTableExists(String tableName) throws SQLException {
        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet rs = dmd.getTables(null,null,tableName, null);
        return  rs.next();
    }

    /**
     * Creates a new table in the connected database with the specified name and column definitions.
     *
     * @param tableName          The name of the table to be created.
     * @param columnDefinitions  An array of strings representing the column definitions for the new table.
     *                           Each string should define a column along with its data type and constraints.
     * @throws SQLException       If a database access error occurs or an error with the SQL operation.
     */
    public void createTable(String tableName, String[] columnDefinitions) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append(" (")
                .append(String.join(", ", columnDefinitions))
                .append(")");

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(queryBuilder.toString());
        } catch (SQLException e) {
            System.out.println("Error: No table created");
        }
    }

    /**
     * Deletes a table from the connected database if it exists.
     *
     * @param tableName The name of the table to be deleted.
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     */
    public void deleteTable(String tableName) throws SQLException{
        String query = "DROP TABLE IF EXISTS " + tableName;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    /**
     * Creates initial tables in the connected database if they do not already exist.
     * The method checks for the existence of 'Images' and 'Users' tables. If either table does not exist,
     * it creates both tables with predefined column definitions.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     */
    public void createInitialTables() throws SQLException {
        Boolean imagesTable = checkTableExists("Images");
        Boolean usersTable = checkTableExists("Users");
        if (imagesTable & usersTable){
            return;
        }
        else{
            String[] imagesColumns = {
                    "_id INT PRIMARY KEY AUTO_INCREMENT",
                    "_username VARCHAR(50) NOT NULL",
                    "imageURL VARCHAR(200) NOT NULL",
                    "imageName VARCHAR(100)",
                    "imageHash VARCHAR(200) NOT NULL",
                    "analysisResult VARCHAR(1000)"
            };
            String[] usersColumns = {
                    "_username VARCHAR(50) PRIMARY KEY",
                    "firstname VARCHAR(100)",
                    "lastname VARCHAR(100) NOT NULL",
                    "email VARCHAR(200) NOT NULL",
                    "password VARCHAR(100) NOT NULL"
            };
            createTable("Images", imagesColumns);
            createTable("Users", usersColumns);

        }
    }

    /**
     * Inserts test data into the existing 'Images' and 'Users' tables in the connected database.
     * This method is intended for testing purposes. It inserts a test record into both tables assuming they already exist.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     */
    public void createTestTables() throws SQLException {
        insertIntoImages("testUser","testURL","imageName",
                "exampleHash","This is a test photo.");
        insertIntoUsers("testUser", "First", "Last",
                "abc@def.com","testPassword");

    }

    /**
     * Closes the connection to the database if it is open.
     * If the connection is not null and is not already closed, this method closes it.
     * Any exceptions that occur during the closing process are printed to the standard error stream.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception
        }
    }

    /**
     * Inserts a new record into the 'Images' table with the provided data.
     *
     * @param username       The username associated with the image.
     * @param imageUrl       The URL of the image.
     * @param imageName      The name of the image.
     * @param imageHash      The hash value of the image.
     * @param analysisResult The result of the image analysis.
     * @throws SQLException  If a database access error occurs or an error with the SQL operation.
     */
    public void insertIntoImages(String username, String imageUrl, String imageName,
                                 String imageHash, String analysisResult) throws SQLException {
        String query = " insert into Images (_username, imageUrl, imageName, imageHash, " +
                "analysisResult)"
                + " values (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, imageUrl);
            ps.setString(3, imageName);
            ps.setString(4, imageHash);
            ps.setString(5, analysisResult);

            ps.execute();
        } catch (SQLException e) {
            System.out.println("Error: No image created");
        }
    }

    /**
     * Inserts a new record into the 'Users' table with the provided user data.
     *
     * @param username   The username of the user.
     * @param firstName  The first name of the user.
     * @param lastName   The last name of the user.
     * @param email      The email address of the user.
     * @param password   The password of the user.
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     */
    public void insertIntoUsers(String username, String firstName, String lastName,
                                String email, String password) throws SQLException {
        String query = " insert into Users (_username, firstName, lastName, email, " +
                "password)"
                + " values (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, email);
            ps.setString(5, password);

            ps.execute();
        } catch (SQLException e){
            System.out.println("Error: No user created");
        }
    }

    /**
     * Checks if a specific item exists in a specified column of a given table in the connected database.
     *
     * @param table  The name of the table to check.
     * @param column The name of the column within the table.
     * @param item   The value to check for in the specified column.
     * @return {@code true} if the item exists in the specified column, {@code false} otherwise.
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     */
    public Boolean checkItem(String table, String column, String item) throws SQLException {
        String query = " select * from "+ table +" where "+ column +" = '" + item +"'";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, item);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            } catch (SQLException e) {
                return false;
            }
        }
    }

    /**
     * Retrieves information from a specified column of a given table in the connected database based on a restriction.
     *
     * @param table       The name of the table to retrieve information from.
     * @param column      The name of the column within the table.
     * @param restriction The value used as a restriction in the WHERE clause.
     * @return A ResultSet containing the retrieved information, or {@code null} if an error occurs.
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     */
    public ResultSet returnInfo(String table, String column, String restriction) throws SQLException {
        String query = " select * from " + table + " where "+ column +" = '" + restriction + "'";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, restriction);
            return ps.executeQuery();
        } catch (SQLException e) {
            return null;
        }
    }
}
