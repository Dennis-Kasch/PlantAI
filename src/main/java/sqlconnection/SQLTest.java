package sqlconnection;
/**
 * The SQLTest class provides methods for testing the functionalities of the SQLConnector class.
 * Each method represents a specific test case, covering various scenarios related to database operations.
 * <p>
 * The test cases include connecting to the database, creating and deleting tables, inserting data,
 * and performing queries on the connected database. Each method demonstrates a specific aspect of the
 * SQLConnector class's capabilities.
 * <p>
 * To use this class, create an instance of SQLTest and invoke the desired test method. Ensure that
 * the database connection details and configurations are correctly set in the 'db.properties' file.
 * </p>
 *
 * @see SQLConnector
 * @author Daniel Hahn
 */
import java.sql.SQLException;

public class SQLTest {

    /**
     * Test case: db0101
     * <p>
     * Connects to the database using SQLConnector and tests the closure of the connection.
     * This test verifies the ability to establish a database connection.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0101() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.closeConnection();
    }

    /**
     * Test case: db0102
     * <p>
     * Attempts to connect to the database using SQLConnector with a potentially incorrect path to the properties file.
     * The method tests and handles scenarios where the properties file path might be incorrect.
     * This ensures the proper handling of resource loading failures.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0102() throws SQLException {
        //to be changed
        SQLConnector dbc = new SQLConnector();
        dbc.closeConnection();
    }

    /**
     * Test case: db0103
     * <p>
     * Attempts to connect to the database using SQLConnector when the database is offline or unreachable.
     * The method tests and handles scenarios where the database connection cannot be established.
     * This ensures the proper handling of offline database situations.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0103() throws SQLException {
        //to be changed
        SQLConnector dbc = new SQLConnector();
        dbc.closeConnection();
    }

    /**
     * Test case: db0201
     * <p>
     * Connects to the database using SQLConnector, creates a table named "Test" with specified columns,
     * and checks if the table exists. The method verifies the accuracy of the checkTableExists method,
     * ensuring it correctly detects the existence of the created table.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0301
     * <p>
     * Connects to the database using SQLConnector, deletes a table named "Test" if it exists,
     * creates a new table named "Test" with specified columns using the createTable method,
     * and checks if the table exists. The method verifies the accurate functioning of the createTable method
     * and ensures the successful creation and existence of the "Test" table in the database.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0302
     * <p>
     * Connects to the database using SQLConnector, attempts to create a table named "Test" twice with the same columns,
     * and checks if the table exists. The method verifies the behavior of the createTable method
     * when trying to create a table that already exists, ensuring it does not create duplicate tables
     * and correctly detects the existence of the table in the database.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0303
     * <p>
     * Connects to the database using SQLConnector, deletes a table named "Test" if it exists,
     * attempts to create a table named "Test" with incorrect syntax for the columns,
     * and checks if the table exists. The method verifies the createTable method's behavior
     * when encountering an incorrect syntax for column definitions, ensuring it handles the error gracefully
     * and does not create the table. The checkTableExists method is then used to confirm the absence of the table.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */

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

    /**
     * Test case: db0304
     * <p>
     * Connects to the database using SQLConnector, attempts to create a table named "Test" with correct columns,
     * then attempts to create the same table with incorrect syntax for the columns,
     * and checks if the table still exists. The method verifies the createTable method's behavior
     * when trying to create a table with correct columns followed by incorrect syntax, ensuring it handles errors
     * and does not affect the existence of the table. The checkTableExists method is then used to confirm the presence of the table.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0401
     * <p>
     * Connects to the database using SQLConnector, deletes existing "Users" and "Images" tables,
     * invokes createInitialTables to create the initial "Users" and "Images" tables,
     * and checks if the tables exist. The method tests the createInitialTables function's ability
     * to create tables with predefined columns when the tables don't already exist.
     * The checkTableExists method is then used to confirm the presence of both tables.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0401() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        System.out.println(dbc.checkTableExists("Users"));
        System.out.println(dbc.checkTableExists("Images"));
        dbc.closeConnection();
    }

    /**
     * Test case: db0402
     * <p>
     * Connects to the database using SQLConnector, invokes createInitialTables twice in succession,
     * and checks if the "Users" and "Images" tables exist. The method tests the createInitialTables function's behavior
     * when attempting to create already existing tables, ensuring it doesn't create duplicate tables.
     * The checkTableExists method is then used to confirm the presence of both tables.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0402() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.createInitialTables();
        dbc.createInitialTables();
        System.out.println(dbc.checkTableExists("Users"));
        System.out.println(dbc.checkTableExists("Images"));
        dbc.closeConnection();
    }

    /**
     * Test case: db0403
     * <p>
     * Connects to the database using SQLConnector, invokes createInitialTables,
     * deletes the "Images" table, and then invokes createInitialTables again.
     * The method tests the createInitialTables function's behavior when one of the required tables ("Images") is deleted
     * before attempting to recreate them, ensuring it recreates the missing table.
     * The checkTableExists method is then used to confirm the presence of both tables.
     * If the "Images" table did not exist initially, it verifies that the method creates it.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0403() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.createInitialTables();
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        System.out.println(dbc.checkTableExists("Users"));
        System.out.println(dbc.checkTableExists("Images"));
        dbc.closeConnection();
    }

    /**
     * Test case: db0404
     * <p>
     * Connects to the database using SQLConnector, invokes createInitialTables,
     * deletes the "Users" table, and then invokes createInitialTables again.
     * The method tests the createInitialTables function's behavior when one of the required tables ("Users") is deleted
     * before attempting to recreate them. It specifically focuses on checking if the "Users" table is recreated.
     * The checkTableExists method is then used to confirm the presence of both tables.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0404() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.createInitialTables();
        dbc.deleteTable("Users");
        dbc.createInitialTables();
        System.out.println(dbc.checkTableExists("Users"));
        System.out.println(dbc.checkTableExists("Images"));
        dbc.closeConnection();
    }

    /**
     * Test case: db0501
     * <p>
     * Connects to the database using SQLConnector and immediately closes the connection.
     * The method tests the functionality of the closeConnection method, ensuring that the database connection
     * is properly closed without any errors.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0501() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.closeConnection();
    }

    /**
     * Test case: db0601
     * <p>
     * Connects to the database using SQLConnector, deletes the "Images" and "Users" tables,
     * creates initial tables using createInitialTables, and then invokes createTestTables.
     * The method tests the createTestTables function's behavior by populating the "Images" and "Users" tables with test data.
     * It also uses the returnInfo method to retrieve information from both tables and prints the results.
     * The primary focus is on ensuring that createTestTables successfully inserts test data into the tables.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0602
     * <p>
     * Connects to the database using SQLConnector, deletes the "Images" and "Users" tables,
     * and invokes createTestTables without creating initial tables.
     * The method tests the createTestTables function's behavior by attempting to populate the "Images" and "Users" tables with test data
     * without the existence of the tables. It uses the returnInfo method to retrieve information from both tables and prints the results.
     * The primary focus is on ensuring that createTestTables handles the absence of tables gracefully and does not cause errors.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0603
     * <p>
     * Connects to the database using SQLConnector, deletes the "Images" and "Users" tables,
     * creates initial tables using createInitialTables, and deletes the "Users" table again.
     * Then invokes createTestTables to populate the tables with test data.
     * The method tests the createTestTables function's behavior when invoked after creating initial tables
     * and then deleting one of the tables it attempts to populate.
     * It uses the returnInfo method to retrieve information from both tables and prints the results.
     * The primary focus is on ensuring that createTestTables handles the absence of "Users" table gracefully and does not cause errors.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0604
     * <p>
     * Connects to the database using SQLConnector, deletes the "Images" and "Users" tables,
     * creates initial tables using createInitialTables, and deletes the "Users" table again.
     * Then invokes createTestTables to populate the tables with test data.
     * The method tests the createTestTables function's behavior when invoked after creating initial tables
     * and then deleting one of the tables it attempts to populate.
     * It uses the returnInfo method to retrieve information from both tables and prints the results.
     * The primary focus is on ensuring that createTestTables handles the absence of "Images" table gracefully and does not cause errors.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0701
     * <p>
     * Connects to the database using SQLConnector, deletes the "Images" and "Users" tables,
     * creates initial tables using createInitialTables, and inserts a test image into the "Images" table using insertIntoImages.
     * It then retrieves information about the inserted image using returnInfo and prints the results.
     * The primary focus is on testing the insertion of an image record into the "Images" table.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0702
     * <p>
     * Connects to the database using SQLConnector, deletes the "Images" and "Users" tables,
     * inserts a test image into the "Images" table using insertIntoImages, and retrieves information
     * about the inserted image using returnInfo. It then prints the results.
     * It assesses how the function handles the situation when the "Images" table does not exist.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0801
     * <p>
     * Connects to the database using SQLConnector, deletes the "Images" and "Users" tables,
     * creates initial tables using createInitialTables, and inserts a test user into the "Users" table
     * using insertIntoUsers. It then retrieves information about the inserted user and prints the results.
     * The primary focus is on testing the insertion of a user record into the "Users" table after creating initial tables.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0802
     * <p>
     * Connects to the database using SQLConnector, deletes the "Images" and "Users" tables,
     * inserts a test user into the "Users" table using insertIntoUsers.
     * It then retrieves information about the inserted user and prints the results.
     * It assesses how the function handles the situation when the "Users" table does not exist.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
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

    /**
     * Test case: db0901
     * <p>
     * Connects to the database using SQLConnector, deletes the "Users" and "Images" tables,
     * creates initial tables using createInitialTables, inserts a test user into the "Users" table using insertIntoUsers,
     * and checks if the inserted user with the specified first name ("First") exists in the "Users" table using checkItem.
     * The primary focus is on testing the checkItem function to verify the existence of a specific item in the "Users" table.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0901() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        dbc.insertIntoUsers("testUser", "First", "Last",
                "abc@def.com", "testPassword");
        System.out.println(dbc.checkItem("Users", "firstName", "First"));
        dbc.closeConnection();
    }

    /**
     * Test case: db0902
     * <p>
     * Connects to the database using SQLConnector, deletes the "Users" and "Images" tables,
     * creates initial tables using createInitialTables, inserts two test users into the "Users" table using insertIntoUsers,
     * and checks if the inserted user with the specified first name ("First") exists in the "Users" table using checkItem.
     * The primary focus is on testing the checkItem function to verify the existence of a specific item in the "Users" table
     * when multiple items with the same attribute value are present.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0902() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        dbc.insertIntoUsers("testUser", "First", "Last",
                "abc@def.com", "testPassword");
        dbc.insertIntoUsers("testUser2", "First", "Last",
                "abc@def.com", "testPassword");
        System.out.println(dbc.checkItem("Users", "firstName", "First"));
        dbc.closeConnection();
    }

    /**
     * Test case: db0903
     * <p>
     * Connects to the database using SQLConnector, deletes the "Users" and "Images" tables,
     * creates initial tables using createInitialTables, and checks if an item with the specified first name ("First")
     * exists in the "Users" table using checkItem.
     * The primary focus is on testing the checkItem function to verify the absence of the specified item in the "Users" table.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0903() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        System.out.println(dbc.checkItem("Users", "firstName", "First"));
        dbc.closeConnection();
    }

    /**
     * Test case: db0904
     * <p>
     * Connects to the database using SQLConnector, deletes the "Users" and "Images" tables,
     * and checks if an item with the specified first name ("First") exists in the "Users" table using checkItem.
     * The primary focus is on testing the checkItem function to verify its behavior when a table does not exist.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0904() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        System.out.println(dbc.checkItem("Users", "firstName", "First"));
        dbc.closeConnection();
    }

    /**
     * Test case: db0905
     * <p>
     * Connects to the database using SQLConnector, deletes the "Users" and "Images" tables,
     * creates initial tables, and checks if an item with the specified first name ("First") exists in the "Users" table using checkItem.
     * The primary focus is on testing the checkItem function to verify its behavior when the specified column ("first") does not exist.
     *
     * @throws SQLException If a database access error occurs or an error with the SQL operation.
     * @see SQLConnector
     */
    public void db0905() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        System.out.println(dbc.checkItem("Users", "first", "First"));
        dbc.closeConnection();
    }

    /**
     * Test case: db1001
     * <p>
     * Connects to the database using SQLConnector, deletes the "Users" and "Images" tables,
     * creates initial tables, inserts a user record with specified details, and retrieves
     * information about the user based on the first name.
     * The primary focus is on testing the returnInfo function to ensure its correctness.
     *
     * @throws SQLException if a database access error occurs or an error with the SQL operation
     * @see SQLConnector
     */
    public void db1001() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        dbc.insertIntoUsers("testUser", "First", "Last",
                "abc@def.com", "testPassword");
        System.out.println(dbc.returnInfo("Users", "firstName", "First"));
        dbc.closeConnection();
    }

    /**
     * Test case: db1002
     * <p>
     * Connects to the database using SQLConnector, deletes the "Users" and "Images" tables,
     * creates initial tables, inserts two user records with specified details, and retrieves
     * information about the users based on the first name.
     * The primary focus is on testing the returnInfo function to ensure its correctness when multiple items exist.
     *
     * @throws SQLException if a database access error occurs or an error with the SQL operation
     * @see SQLConnector
     */
    public void db1002() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        dbc.insertIntoUsers("testUser", "First", "Last",
                "abc@def.com", "testPassword");
        dbc.insertIntoUsers("testUser2", "First", "Last",
                "abc@def.com", "testPassword");
        System.out.println(dbc.returnInfo("Users", "firstName", "First"));
        dbc.closeConnection();
    }

    /**
     * Test case: db1003
     * <p>
     * Connects to the database using SQLConnector, deletes the "Users" and "Images" tables,
     * creates initial tables, and retrieves information about users based on the specified first name.
     * The primary focus is on testing the returnInfo function to ensure its behavior when the specified item does not exist.
     *
     * @throws SQLException if a database access error occurs or an error with the SQL operation
     * @see SQLConnector
     */
    public void db1003() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        System.out.println(dbc.returnInfo("Users", "firstName", "First"));
        dbc.closeConnection();
    }

    /**
     * Test case: db1004
     * <p>
     * Connects to the database using SQLConnector, deletes the "Users" and "Images" tables,
     * and retrieves information about users based on the specified first name.
     * The primary focus is on testing the returnInfo function to ensure its behavior when the table does not exist.
     *
     * @throws SQLException if a database access error occurs or an error with the SQL operation
     * @see SQLConnector
     */
    public void db1004() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        System.out.println(dbc.returnInfo("Users", "firstName", "First"));
        dbc.closeConnection();
    }

    /**
     * Test case: db1005
     * <p>
     * Connects to the database using SQLConnector, deletes the "Users" and "Images" tables,
     * creates initial tables, and retrieves information about users based on a column that does not exist ("first").
     * The primary focus is on testing the returnInfo function to verify its behavior when the specified column does not exist.
     *
     * @throws SQLException if a database access error occurs or an error with the SQL operation
     * @see SQLConnector
     */
    public void db1005() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("Users");
        dbc.deleteTable("Images");
        dbc.createInitialTables();
        System.out.println(dbc.returnInfo("Users", "first", "First"));
        dbc.closeConnection();
    }

    /**
     * Test case: db1101
     * <p>
     * Connects to the database using SQLConnector, creates initial tables, deletes the "Images" table,
     * and checks if the "Images" table still exists using checkTableExists.
     * The primary focus is on testing the deleteTable function and verifying its impact on the existence of the specified table.
     *
     * @throws SQLException if a database access error occurs or an error with the SQL operation
     * @see SQLConnector
     */
    public void db1101() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.createInitialTables();
        dbc.deleteTable("Images");
        System.out.println(dbc.checkTableExists("Images"));
        dbc.closeConnection();
    }

    /**
     * Test case: db1102
     * <p>
     * Connects to the database using SQLConnector, attempts to delete a table ("RandomTableThatDoesNotExist"),
     * and checks if the table still exists using checkTableExists.
     * The primary focus is on testing the deleteTable function and its behavior when attempting to delete a nonexistent table.
     *
     * @throws SQLException if a database access error occurs or an error with the SQL operation
     * @see SQLConnector
     */
    public void db1102() throws SQLException {
        SQLConnector dbc = new SQLConnector();
        dbc.deleteTable("RandomTableThatDoesNotExist");
        System.out.println(dbc.checkTableExists("RandomTableThatDoesNotExist"));
        dbc.closeConnection();
    }
}
