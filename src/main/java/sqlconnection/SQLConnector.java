package sqlconnection;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class SQLConnector {

    private Connection connection;

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

    public Boolean checkTableExists(String tableName) throws SQLException {
        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet rs = dmd.getTables(null,null,tableName, null);
        return  rs.next();
    }

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

    public void deleteTable(String tableName) throws SQLException{
        String query = "DROP TABLE IF EXISTS " + tableName;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

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

    public void createTestTables() throws SQLException {
        insertIntoImages("testUser","testURL","imageName",
                "exampleHash","This is a test photo.");
        insertIntoUsers("testUser", "First", "Last",
                "abc@def.com","testPassword");

    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception
        }
    }

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
