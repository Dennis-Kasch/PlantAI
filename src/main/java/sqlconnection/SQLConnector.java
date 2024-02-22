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
        }
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

    public void insertIntoPhotos(String url, String info) throws SQLException {
        String query = " insert into photos (url, result)"
                + " values (?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString (1, url);
        ps.setString (2, info);
        ps.execute();
    }

    public Boolean checkPhotoUrl(String url) throws SQLException {
        String query = " select * from photos where url = '" + url +"'";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }

    public ResultSet returnInfo(String table, String restriction) throws SQLException {
        String query = " select * from " + table + " where url = '" + restriction + "'";
        PreparedStatement ps = connection.prepareStatement(query);
        return ps.executeQuery();
    }
}
