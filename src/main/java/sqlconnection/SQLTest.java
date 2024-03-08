package sqlconnection;

import java.sql.SQLException;

public class SQLTest {

    public void db0101() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.closeConnection();
    }

    public void db0102() throws SQLException {
        //to be changed
        SQLConnector dbc = new SQLConnector();
        dbc.closeConnection();
    }

    public void db0103() throws SQLException {
        //to be changed
        SQLConnector dbc = new SQLConnector();
        dbc.closeConnection();
    }

    public void db0201() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        String[] columns = {
                "_id INT PRIMARY KEY AUTO_INCREMENT",
                "_username VARCHAR(50) NOT NULL",
                "imageURL VARCHAR(200) NOT NULL",
                "imageName VARCHAR(100)",
                "imageHash VARCHAR(200) NOT NULL",
                "analysisResult VARCHAR(1000)"
        };
        dbc.createTable("Test", columns);
        System.out.println(dbc.checkTableExists("Test"));
        dbc.closeConnection();
    }

    public void db0202() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Test");
        System.out.println(dbc.checkTableExists("Test"));
        dbc.closeConnection();
    }

    public void db0301() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        String[] columns = {
                "_id INT PRIMARY KEY AUTO_INCREMENT",
                "_username VARCHAR(50) NOT NULL",
                "imageURL VARCHAR(200) NOT NULL",
                "imageName VARCHAR(100)",
                "imageHash VARCHAR(200) NOT NULL",
                "analysisResult VARCHAR(1000)"
        };
        dbc.deleteTable("Test");
        dbc.createTable("Test", columns);
        System.out.println(dbc.checkTableExists("Test"));
        dbc.closeConnection();
    }

    public void db0302() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        String[] columns = {
                "_id INT PRIMARY KEY AUTO_INCREMENT",
                "_username VARCHAR(50) NOT NULL",
                "imageURL VARCHAR(200) NOT NULL",
                "imageName VARCHAR(100)",
                "imageHash VARCHAR(200) NOT NULL",
                "analysisResult VARCHAR(1000)"
        };
        dbc.createTable("Test", columns);
        dbc.createTable("Test", columns);
        System.out.println(dbc.checkTableExists("Test"));
        dbc.closeConnection();
    }

    public void db0303() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        String[] columns = {
                "A wrong syntax",
        };
        dbc.deleteTable("Test");
        dbc.createTable("Test", columns);
        System.out.println(dbc.checkTableExists("Test"));
        dbc.closeConnection();
    }

    public void db0304() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        String[] columns = {
                "_id INT PRIMARY KEY AUTO_INCREMENT",
                "_username VARCHAR(50) NOT NULL",
                "imageURL VARCHAR(200) NOT NULL",
                "imageName VARCHAR(100)",
                "imageHash VARCHAR(200) NOT NULL",
                "analysisResult VARCHAR(1000)"
        };
        dbc.createTable("Test", columns);
        String[] columnsWrong = {
                "A wrong syntax",
        };
        dbc.createTable("Test", columnsWrong);
        System.out.println(dbc.checkTableExists("Test"));
        dbc.closeConnection();
    }

    public void db0401() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        System.out.println(dbc.checkTableExists("Users"));
        System.out.println(dbc.checkTableExists("Images"));
        dbc.closeConnection();
    }

    public void db0402() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.createInitialTables();
        dbc.createInitialTables();
        System.out.println(dbc.checkTableExists("Users"));
        System.out.println(dbc.checkTableExists("Images"));
        dbc.closeConnection();
    }

    public void db0403() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.createInitialTables();
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        System.out.println(dbc.checkTableExists("Users"));
        System.out.println(dbc.checkTableExists("Images"));
        dbc.closeConnection();
    }

    public void db0404() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.createInitialTables();
        dbc.deleteTable("Users");
        dbc.createInitialTables();
        System.out.println(dbc.checkTableExists("Users"));
        System.out.println(dbc.checkTableExists("Images"));
        dbc.closeConnection();
    }

    public void db0501() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.closeConnection();
    }

    public void db0601() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Images");
        dbc.deleteTable("Users");
        dbc.createInitialTables();
        dbc.createTestTables();
        System.out.println(dbc.returnInfo("Images", "imageName",
                "imageName"));
        System.out.println(dbc.returnInfo("Users", "firstName",
                "First"));
        dbc.closeConnection();
    }

    public void db0602() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Images");
        dbc.deleteTable("Users");
        dbc.createTestTables();
        System.out.println(dbc.returnInfo("Images", "imageName",
                "imageName"));
        System.out.println(dbc.returnInfo("Users", "firstName",
                "First"));
        dbc.closeConnection();
    }

    public void db0603() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Images");
        dbc.deleteTable("Users");
        dbc.createInitialTables();
        dbc.deleteTable("Users");
        dbc.createTestTables();
        System.out.println(dbc.returnInfo("Images", "imageName",
                "imageName"));
        System.out.println(dbc.returnInfo("Users", "firstName",
                "First"));
        dbc.closeConnection();
    }

    public void db0604() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Images");
        dbc.deleteTable("Users");
        dbc.createInitialTables();
        dbc.deleteTable("Images");
        dbc.createTestTables();
        System.out.println(dbc.returnInfo("Images", "imageName",
                "imageName"));
        System.out.println(dbc.returnInfo("Users", "firstName",
                "First"));
        dbc.closeConnection();
    }

    public void db0701() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Images");
        dbc.deleteTable("Users");
        dbc.createInitialTables();
        dbc.insertIntoImages("testUser", "testURL", "imageName",
                "exampleHash", "This is a test photo.");
        System.out.println(dbc.returnInfo("Images", "imageName",
                "imageName"));
        dbc.closeConnection();
    }

    public void db0702() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Images");
        dbc.deleteTable("Users");
        dbc.insertIntoImages("testUser", "testURL", "imageName",
                "exampleHash", "This is a test photo.");
        System.out.println(dbc.returnInfo("Images", "imageName",
                "imageName"));
        dbc.closeConnection();
    }

    public void db0801() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Images");
        dbc.deleteTable("Users");
        dbc.createInitialTables();
        dbc.insertIntoUsers("testUser", "First", "Last",
                "abc@def.com", "testPassword");
        System.out.println(dbc.returnInfo("Users", "firstName",
                "First"));
        dbc.closeConnection();
    }

    public void db0802() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Images");
        dbc.deleteTable("Users");
        dbc.insertIntoUsers("testUser", "First", "Last",
                "abc@def.com", "testPassword");
        System.out.println(dbc.returnInfo("Users", "firstName",
                "First"));
        dbc.closeConnection();
    }
}
