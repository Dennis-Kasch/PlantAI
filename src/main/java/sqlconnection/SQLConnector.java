package sqlconnection;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

public class SQLConnector {

    private Connection connection;

    public SQLConnector() throws SQLException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find properties");
                return;
            }
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
        addImage("testUser","testURL","imageName",
                "exampleHash","This is a test photo.");
        createUser("testUser", "First", "Last",
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

    public boolean addImage(String username, String imageUrl, String imageName,
    String imageHash, String analysisResult) throws SQLException {
        // First, check if an image with the same hash already exists
        String checkQuery = "SELECT COUNT(*) FROM Images WHERE imageHash = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, imageHash);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
        } 
        catch (SQLException e) {
            throw e;
        }

        // If no image exists with the given hash, proceed to insert the new image
        String insertQuery = "INSERT INTO Images (_username, imageUrl, imageName, imageHash, analysisResult)"
        + " VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
        insertStmt.setString(1, username);
        insertStmt.setString(2, imageUrl);
        insertStmt.setString(3, imageName);
        insertStmt.setString(4, imageHash);
        insertStmt.setString(5, analysisResult);

        int rowsAffected = insertStmt.executeUpdate();
        return rowsAffected > 0; // Returns true if the image was successfully added
        } 
        catch (SQLException e) {
            throw e; 
        }
    }

    public boolean createUser(String username, String firstName, String lastName,
    String email, String password) throws SQLException {
        // Check if a user with the given username already exists
        String checkQuery = "SELECT COUNT(*) FROM Users WHERE _username = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
        } 
        catch (SQLException e) {
            throw e;
        }
        // If no user exists with the given username, proceed to insert the new user
        String insertQuery = "INSERT INTO Users (_username, firstName, lastName, email, password)"+ " VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, username);
            insertStmt.setString(2, firstName);
            insertStmt.setString(3, lastName);
            insertStmt.setString(4, email);
            insertStmt.setString(5, password);
            int rowsAffected = insertStmt.executeUpdate();
            return rowsAffected > 0;
        } 
        catch (SQLException e) {
            throw e;
        }
    }


    public Boolean checkItem(String table, String column, String item) throws SQLException {
        String query = " select * from "+ table +" where "+ column +" = '" + item +"'";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }

    public ResultSet returnInfo(String table, String column, String restriction) throws SQLException {
        String query = " select * from " + table + " where "+ column +" = '" + restriction + "'";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        return rs;
    }

    public boolean doesUserExist(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE _username = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public String getUserHistory(String username) throws SQLException {
        String query = "SELECT * FROM images WHERE _username = ?";
        JSONArray jsonArray = new JSONArray();
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JSONObject record = new JSONObject();
                    record.put("id", rs.getInt("id"));
                    record.put("imageUrl", rs.getString("imageUrl"));
                    record.put("imageName", rs.getString("imageName"));
                    record.put("imageHash", rs.getString("imageHash"));
                    record.put("analysisResult", rs.getString("analysisResult"));
                    jsonArray.put(record);
                }
            }
        }
        return jsonArray.length() > 0 ? jsonArray.toString() : null;
    }

}
