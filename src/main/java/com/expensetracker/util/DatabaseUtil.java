package com.expensetracker.util;

import java.sql.*;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:sqlite:expense_tracker.db";
    private static DatabaseUtil instance;
    private Connection connection;

    private DatabaseUtil() {
        try {
            // Make sure SQLite JDBC driver is available
            Class.forName("org.sqlite.JDBC");
            System.out.println("Connecting to database at: " + DB_URL);
            connection = DriverManager.getConnection(DB_URL);
            
            // Set auto-commit to true for simplicity
            connection.setAutoCommit(true);
            
            System.out.println("Connection established successfully");
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Check if connection is closed or invalid
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection was closed, reconnecting...");
                connection = DriverManager.getConnection(DB_URL);
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection: " + e.getMessage());
            e.printStackTrace();
        }
        
        return connection;
    }

    private void initializeDatabase() {
        try (Statement statement = connection.createStatement()) {
            System.out.println("Initializing database tables...");
            
            // Create categories table
            String createCategoriesTable = 
                "CREATE TABLE IF NOT EXISTS categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL UNIQUE)";
            
            statement.execute(createCategoriesTable);
            System.out.println("Categories table created or already exists");

            // Create transactions table
            String createTransactionsTable = 
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL NOT NULL, " +
                "type TEXT NOT NULL, " +  // 'INCOME' or 'EXPENSE'
                "category_id INTEGER, " +
                "date TEXT NOT NULL, " +
                "description TEXT, " +
                "FOREIGN KEY (category_id) REFERENCES categories(id))";
            
            statement.execute(createTransactionsTable);
            System.out.println("Transactions table created or already exists");
            
            // Check if categories exist
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM categories");
            int categoryCount = 0;
            if (rs.next()) {
                categoryCount = rs.getInt(1);
            }
            rs.close();
            
            System.out.println("Found " + categoryCount + " existing categories");
            
            // Insert default categories if none exist
            if (categoryCount == 0) {
                System.out.println("No categories found, inserting defaults...");
                String[] defaultCategories = {"Food", "Transportation", "Housing", "Utilities", "Entertainment", "Shopping", "Healthcare", "Salary", "Other"};
                
                for (String category : defaultCategories) {
                    try {
                        String insertSql = "INSERT INTO categories (name) VALUES (?)";
                        PreparedStatement pstmt = connection.prepareStatement(insertSql);
                        pstmt.setString(1, category);
                        pstmt.executeUpdate();
                        pstmt.close();
                        System.out.println("Inserted default category: " + category);
                    } catch (SQLException e) {
                        System.err.println("Error inserting category '" + category + "': " + e.getMessage());
                    }
                }
            }
            
            // Verify categories were inserted
            rs = statement.executeQuery("SELECT COUNT(*) FROM categories");
            if (rs.next()) {
                System.out.println("Total categories after initialization: " + rs.getInt(1));
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 