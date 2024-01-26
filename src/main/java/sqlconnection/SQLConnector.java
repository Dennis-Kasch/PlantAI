package sqlconnection;

import java.sql.*;

public class SQLConnector {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/plantaid?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "user1234";

    private Connection connection;

    public SQLConnector() throws SQLException {
        this.connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
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
